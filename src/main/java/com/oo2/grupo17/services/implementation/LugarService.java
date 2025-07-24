package com.oo2.grupo17.services.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.repositories.ILocalidadRepository;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IProvinciaRepository;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.repositories.ITurnoRepository;
import com.oo2.grupo17.services.ILugarService;

import lombok.Builder;

@Service @Builder
public class LugarService implements ILugarService {
	
	private final ILugarRepository lugarRepository;
	private final IServicioRepository servicioRepository;
	private final ILocalidadRepository localidadRepository;
	private final IProvinciaRepository provinciaRepository;
	private final ITurnoRepository turnoRepository;
	private final ModelMapper modelMapper;

	@Override
	public LugarDto save(LugarDto lugarDto) {
		
		Localidad localidad = localidadRepository.findById(lugarDto.getDireccion().getLocalidadId())
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la localidad con ID: " 
						+ lugarDto.getDireccion().getLocalidadId()));
		Provincia provincia = provinciaRepository.findById(lugarDto.getDireccion().getProvinciaId())
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró la provincia con ID: " 
						+ lugarDto.getDireccion().getProvinciaId()));
		
		if (lugarRepository.existsByDireccion_CalleAndDireccion_AlturaAndDireccion_LocalidadAndDireccion_Provincia(
		        lugarDto.getDireccion().getCalle(),
		        lugarDto.getDireccion().getAltura(),
		        localidad, provincia)) {
			throw new EntidadDuplicadaException("El lugar con la dirección ingresada ya existe.");
		}
		Lugar lugar = modelMapper.map(lugarDto, Lugar.class);
		Lugar saved = lugarRepository.save(lugar);
		return modelMapper.map(saved, LugarDto.class);
	}

	@Override
	public LugarDto findById(Long id) {
		Lugar lugar = lugarRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el lugar con ID: " + id));
		return modelMapper.map(lugar, LugarDto.class);
	}

	@Override 
	public List<LugarDto> findAll() {
		return lugarRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, LugarDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public LugarDto update(Long id, LugarDto lugarDto) {
		Lugar lugar = lugarRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el lugar con ID: " + id));
		lugar.setHorarioApertura(lugarDto.getHorarioApertura());
		lugar.setHorarioCierre(lugarDto.getHorarioCierre());
		// lugar.setDireccion(lugarDto.getDireccion());
		Lugar updated = lugarRepository.save(lugar);
		return modelMapper.map(updated, LugarDto.class);
	}

	@Override
	public void deleteById(Long id) {
		Lugar lugar = lugarRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el lugar con ID: " + id));
		
		// Remuevo el lugar de los servicios relacionados
		for(Servicio servicio : new HashSet<>(lugar.getServicios())) {
			servicio.getLugares().remove(lugar);
			servicioRepository.save(servicio);
		}
		
		lugarRepository.deleteById(id);
	}
	
	@Override
	public List<Lugar> obtenerLugaresPorServicio(Long servicioId) {
		return lugarRepository.findByServicios_id(servicioId);
	}
	
	@Override
	public List<LugarDto> findAllById(List<Long> lugares) {
		return lugarRepository.findAllById(lugares)
				.stream()
				.map(object -> modelMapper.map(object, LugarDto.class))
				.collect(Collectors.toList());
	};
	
	@Override
	public Map<Long, Long> getCantidadTurnosPorLugar() {
		List<Object[]> resultados = turnoRepository.countTurnosGroupByLugar();
		Map<Long, Long> turnosPorLugar = new HashMap<>();
		for(Object[] resultado : resultados) {
			Long lugarId = (Long) resultado[0];
			Long cantidadTurnos = (Long) resultado[1];
			turnosPorLugar.put(lugarId, cantidadTurnos);
		}
		return turnosPorLugar;
	}

	@Override
	public List<LugarDto> findByCalle(String nombre) {
		if (nombre == null || nombre.isEmpty()) {
			return findAll();
		}
		
		List<Lugar> lugares = lugarRepository.findByDireccion_CalleContainingIgnoreCase(nombre);
		if (lugares.isEmpty()) {
			throw new EntidadNoEncontradaException("No se encontraron lugares con el nombre: " + nombre);
		}
		
		return lugares.stream()
				.map(lugar -> modelMapper.map(lugar, LugarDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public LugarDto findByCalleAndAltura(String direccion, int altura) {
		return modelMapper.map(
				lugarRepository.findByDireccion_CalleAndDireccion_Altura(direccion, altura),
				LugarDto.class
				);
	}

}
