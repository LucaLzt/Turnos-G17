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
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IDisponibilidadRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.services.IDisponibilidadService;

@Service
public class DisponibilidadService implements IDisponibilidadService {
	
	private final IProfesionalRepository profesionalRepository;
	private final IDisponibilidadRepository disponibilidadRepository;
	private final ModelMapper modelMapper;
	
	public DisponibilidadService(IProfesionalRepository profesionalRepository,
			IDisponibilidadRepository disponibilidadRepository, ModelMapper modelMapper) {
		this.profesionalRepository = profesionalRepository;
		this.disponibilidadRepository = disponibilidadRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public DisponibilidadDto save(DisponibilidadDto disponbilidadDto) {
		Disponibilidad disp = modelMapper.map(disponbilidadDto, Disponibilidad.class);
		Disponibilidad saved = disponibilidadRepository.save(disp);
		return modelMapper.map(saved, DisponibilidadDto.class);
	}

	@Override
	public DisponibilidadDto findById(Long id) {
		Disponibilidad disp = disponibilidadRepository.findById(id)
				.orElseThrow();
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
				.orElseThrow();
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

	@Override
	public void generarDisponibilidadesAutomaticas(Long profesionalId, LocalTime horaIncio, LocalTime horaFin,
			Duration duracionTurno, LocalDate fechaInicio, LocalDate fechaFin) {
		
		if(duracionTurno.isZero() || duracionTurno.isNegative()) {
			throw new IllegalArgumentException("La duración del turno debe ser mayor a 0");
		}
		
		Profesional profesional = profesionalRepository.findById(profesionalId)
				.orElseThrow();
		
		List<Disponibilidad> objetos = new ArrayList<>();
		LocalDate fechaActual = fechaInicio;
		
		while(!fechaActual.isAfter(fechaFin)) {
			
			if(!esFinDeSemana(fechaActual)) {
				
				LocalDateTime inicioTurno = LocalDateTime.of(fechaActual, horaIncio);
				LocalDateTime finJornada = LocalDateTime.of(fechaActual, horaFin);
				
				while(inicioTurno.plus(duracionTurno).isBefore(finJornada.plusSeconds(1))) {
					
					Disponibilidad disp = new Disponibilidad();
					disp.setProfesional(profesional);
					disp.setInicio(inicioTurno);
					disp.setDuracion(duracionTurno);
					disp.setOcupado(false);
					
					// objetos.add(modelMapper.map(disp, Disponibilidad.class));
					objetos.add(disp);
					inicioTurno = inicioTurno.plus(duracionTurno);
					
				}
				
			}
			
			fechaActual = fechaActual.plusDays(1);
			
		}
		
		disponibilidadRepository.saveAll(objetos);
		
	}
	
	// --- Métodos Auxiliares (PRIVADOS) --- //
	private boolean esFinDeSemana(LocalDate fecha) {
		return fecha.getDayOfWeek() == DayOfWeek.SATURDAY ||
				fecha.getDayOfWeek() == DayOfWeek.SUNDAY;
	}

}
