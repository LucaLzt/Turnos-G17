package com.oo2.grupo17.services.implementation;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.repositories.IDisponibilidadRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.services.IDisponibilidadService;

import jakarta.transaction.Transactional;
import lombok.Builder;

@Service @Builder
public class DisponibilidadService implements IDisponibilidadService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IDisponibilidadRepository disponibilidadRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public DisponibilidadDto save(DisponibilidadDto disponbilidadDto) {
		Disponibilidad disp = modelMapper.map(disponbilidadDto, Disponibilidad.class);
		Disponibilidad saved = disponibilidadRepository.save(disp);
		return modelMapper.map(saved, DisponibilidadDto.class);
	}

	@Override
	public DisponibilidadDto findById(Long id) {
		Disponibilidad disp = disponibilidadRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el disponibilidad con ID: " + id));
		return modelMapper.map(disp, DisponibilidadDto.class);
	}

	@Override
	public List<DisponibilidadDto> findAll() {
		return disponibilidadRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, DisponibilidadDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public DisponibilidadDto updateOcupacion(Long id, DisponibilidadDto disponbilidadDto) {
		Disponibilidad disp = disponibilidadRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el disponibilidad con ID: " + id));
		if(disp.isOcupado() == false) {
			disp.setOcupado(true);
		} else {
			disp.setOcupado(false);
		}
		Disponibilidad updated = disponibilidadRepository.save(disp);
		return modelMapper.map(updated, DisponibilidadDto.class);
	}

	@Override
	public void deleteById(Long id) {
		disponibilidadRepository.deleteById(id);
	}

	@Override @Transactional
	public void generarDisponibilidadesAutomaticas(Long profesionalId, LocalTime horaIncio, LocalTime horaFin,
			Duration duracionTurno, LocalDate fechaInicio, LocalDate fechaFin) {

		if(duracionTurno.isZero() || duracionTurno.isNegative()) {
			throw new IllegalArgumentException("La duración del turno debe ser mayor a 0");
		}
		
		Profesional profesional = profesionalRepository.findById(profesionalId)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el profesional con ID: " + profesionalId));
		
		List<Disponibilidad> objetos = new ArrayList<>();
		LocalDate fechaActual = fechaInicio;
		
		while(!fechaActual.isAfter(fechaFin)) {
			if(!esFinDeSemana(fechaActual)) {
				
				LocalDateTime inicioTurno = LocalDateTime.of(fechaActual, horaIncio);
				LocalDateTime finJornada = LocalDateTime.of(fechaActual, horaFin);
				
				while(!inicioTurno.plus(duracionTurno).isAfter(finJornada)) {
					if (disponibilidadRepository.existsByProfesionalAndInicio(profesional, inicioTurno)) {
				        throw new EntidadDuplicadaException("Ya existe una disponibilidad para " + profesional.getNombre() + 
				            " el " + inicioTurno);
				    }
					
					Disponibilidad disp = new Disponibilidad();
					disp.setProfesional(profesional);
					disp.setInicio(inicioTurno);
					disp.setDuracion(duracionTurno);
					disp.setOcupado(false);
					
					objetos.add(disp);
					inicioTurno = inicioTurno.plus(duracionTurno);
					
				}
			}
			fechaActual = fechaActual.plusDays(1);
		}
		disponibilidadRepository.saveAll(objetos);
	}
	
	@Override
	public List<DisponibilidadDto> obtenerDisponibilidadesPorProfesionalLibres(Long profesionalId) {
	    LocalDateTime ahora = LocalDateTime.now();
	    return disponibilidadRepository.findByProfesionalIdAndInicioAfterAndOcupadoFalse(profesionalId, ahora)
	        .stream()
	        .map(disponibilidad -> modelMapper.map(disponibilidad, DisponibilidadDto.class))
	        .collect(Collectors.toList());
	}
	
	@Override
	public List<DisponibilidadDto> obtenerDisponibilidadesPorProfesional(Long profesionalId) {
	    LocalDateTime ahora = LocalDateTime.now();
	    return disponibilidadRepository.findByProfesionalIdAndInicioAfter(profesionalId, ahora)
	        .stream()
	        .map(disponibilidad -> modelMapper.map(disponibilidad, DisponibilidadDto.class))
	        .collect(Collectors.toList());
	}
	
	// --- Métodos Auxiliares (PRIVADOS) --- //
	private boolean esFinDeSemana(LocalDate fecha) {
		return fecha.getDayOfWeek() == DayOfWeek.SATURDAY ||
				fecha.getDayOfWeek() == DayOfWeek.SUNDAY;
	}

}
