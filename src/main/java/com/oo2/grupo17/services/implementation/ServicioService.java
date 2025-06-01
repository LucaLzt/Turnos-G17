package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.repositories.IServicioRepository;
import com.oo2.grupo17.services.IServicioService;

@Service
public class ServicioService implements IServicioService {
	
	private final IServicioRepository servicioRepository;
	private final ModelMapper modelMapper;
	
	public ServicioService(IServicioRepository servicioRepository, ModelMapper modelMapper) {
		this.servicioRepository = servicioRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ServicioDto save(ServicioDto servicioDto) {
		Servicio servicio = modelMapper.map(servicioDto, Servicio.class);
		Servicio saved = servicioRepository.save(servicio);
		return modelMapper.map(saved, ServicioDto.class);
	}

	@Override
	public ServicioDto findById(Long id) {
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(servicio, ServicioDto.class);
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
		Servicio servicio = servicioRepository.findById(id)
				.orElseThrow();
		servicio.setNombre(servicioDto.getNombre());
		servicio.setPrecio(servicioDto.getPrecio());
		servicio.setDescripcion(servicioDto.getDescripcion());
		Servicio updated = servicioRepository.save(servicio);
		return modelMapper.map(updated, ServicioDto.class);
	}

	@Override
	public void deleteById(Long id) {
		servicioRepository.deleteById(id);
	}
	
	public List<ServicioDto> findAllByOrderByNombreAsc(){
		return servicioRepository.findAllByOrderByNombreAsc()
				.stream()
				.map(object -> modelMapper.map(object, ServicioDto.class))
				.collect(Collectors.toList());
	}
}
