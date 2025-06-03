package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.services.IServicioService;
import lombok.Builder;


@Service @Builder
public class ServicioService implements IServicioService {
	
	private final IServicioRepository servicioRepository;
	private final ILugarRepository lugarRepository;
	private final ModelMapper modelMapper;

	@Override
	public ServicioDto save(ServicioDto servicioDto) {
		Servicio servicio = modelMapper.map(servicioDto, Servicio.class);
		Servicio saved = servicioRepository.save(servicio);
		return modelMapper.map(saved, ServicioDto.class);
	}

	@Override
	public ServicioDto findById(Long id) {
		// 1. Obtengo el servicio por ID desde la base de datos
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow();
		
		// 2. Mapeo el servicio a ServicioDto
		ServicioDto servicioDto = modelMapper.map(servicio, ServicioDto.class);
		
		// 3. Obtengo los IDs de los lugares asociados al servicio
		List<Long> lugaresIds = servicio.getLugares()
				.stream()
				.map(Lugar::getId)
				.collect(Collectors.toList());
		servicioDto.setLugaresIds(lugaresIds);
		
		// 4. Retorno el servicioDto con los lugaresIds
		return servicioDto;
	}

	@Override
	public List<ServicioDto> findAll() {
		return servicioRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ServicioDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ServicioDto update(Long id, ServicioDto servicioDto) {
		// 1. Obtengo el servicio por ID desde la base de datos
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow();
		
		// 2. Actualizo los campos simples del servicio
		servicio.setNombre(servicioDto.getNombre());
		servicio.setPrecio(servicioDto.getPrecio());
		servicio.setDescripcion(servicioDto.getDescripcion());
		
		// 3. Actualizo la relación muchos a muchos con los lugares
		List<Lugar> lugares = lugarRepository.findAllById(servicioDto.getLugaresIds());
		Set<Lugar> lugaresSet = lugares.stream().collect(Collectors.toSet());
		servicio.setLugares(lugaresSet);
		
		// 4. Guardo los cambios en la base de datos
		Servicio updated = servicioRepository.save(servicio);
		return modelMapper.map(updated, ServicioDto.class);
	}

	@Override
	public void deleteById(Long id) {
		servicioRepository.deleteById(id);
	}
	
	@Override
	public List<ServicioDto> findAllByOrderByNombreAsc(){
		return servicioRepository.findAllByOrderByNombreAsc()
				.stream()
				.map(object -> modelMapper.map(object, ServicioDto.class))
				.collect(Collectors.toList());
	}
	
	
}
