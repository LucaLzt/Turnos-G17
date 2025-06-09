package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Direccion;
import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.repositories.IClienteRepository;
import com.oo2.grupo17.repositories.IDisponibilidadRepository;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.repositories.ITurnoRepository;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.transaction.Transactional;
import lombok.Builder;

@Service @Builder
public class TurnoService implements ITurnoService {
	
	private final ITurnoRepository turnoRepository;
	private final IClienteRepository clienteRepository;
	private final IProfesionalRepository profesionalRepository;
	private final IServicioRepository servicioRepository;
	private final IDisponibilidadRepository disponibilidadRepository;
	private final ILugarRepository lugarRepository;
	private final ModelMapper modelMapper;

	@Override
	public TurnoDto save(TurnoDto turnoDto) {
		Turno turno = modelMapper.map(turnoDto, Turno.class);
		Turno saved = turnoRepository.save(turno);
		return modelMapper.map(saved, TurnoDto.class);
	}

	@Override
	public TurnoDto findById(Long id) {
		Turno turno = turnoRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(turno, TurnoDto.class);
	}

	@Override
	public List<TurnoDto> findAll() {
		return turnoRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, TurnoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {
		turnoRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void crearTurno(TurnoDto turnoDto) {
	    // Buscar entidades gestionadas por Hibernate a partir de los IDs en el DTO
	    Cliente cliente = clienteRepository.findById(turnoDto.getCliente().getId())
	            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
	    Servicio servicio = servicioRepository.findById(turnoDto.getServicio().getId())
	            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
	    Lugar lugar = lugarRepository.findById(turnoDto.getLugar().getId())
	            .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
	    Profesional profesional = profesionalRepository.findById(turnoDto.getProfesional().getId())
	            .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));
	    Disponibilidad disponibilidad = disponibilidadRepository.findById(turnoDto.getDisponibilidad().getId())
	            .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

	    // Crear y poblar la entidad Turno
	    Turno turno = new Turno();
	    turno.setCliente(cliente);
	    turno.setServicio(servicio);
	    turno.setLugar(lugar);
	    turno.setProfesional(profesional);
	    turno.setDisponibilidad(disponibilidad);

	    // Guardar el turno
	    turnoRepository.save(turno);

	    // Marcar la disponibilidad como ocupada si corresponde
	    disponibilidad.setOcupado(true);
	    disponibilidadRepository.save(disponibilidad);
	}
	
	public List<Turno> buscarTurnosPorClienteId(Long clienteId) {
	    return turnoRepository.findByClienteId(clienteId);
	}
	
	@Override
	public List<Turno> buscarTurnosPorProfesionalId(Long profesionalId) {
	    return turnoRepository.findByProfesionalId(profesionalId);
	}
	
	public TurnoDto update(Long id, TurnoDto turnoDto) {
		Turno turno = turnoRepository.findById(id)
			.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el direccion con ID: " + id));
		 Lugar lugar = lugarRepository.findById(turnoDto.getLugar().getId())
		            .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
		 // Disponibilidad disponibilidad = disponibilidadRepository.findById(turnoDto.getDisponibilidad().getId())
		   //         .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
		  turno.setLugar(lugar);
		  //turno.setDisponibilidad(disponibilidad);
		  Turno updated = turnoRepository.save(turno);

		return modelMapper.map(updated, TurnoDto.class);
	}

}
