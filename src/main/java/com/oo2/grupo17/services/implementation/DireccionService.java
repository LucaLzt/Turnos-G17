package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.entities.Direccion;
import com.oo2.grupo17.repositories.IDireccionRepository;
import com.oo2.grupo17.services.IDireccionService;

public class DireccionService implements IDireccionService {
	
	private final IDireccionRepository direccionRepository;
	private final ModelMapper modelMapper;
	
	public DireccionService(IDireccionRepository direccionRepository, ModelMapper modelMapper) {
		this.direccionRepository = direccionRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public DireccionDto save(DireccionDto direccionDto) {
		Direccion direccion = modelMapper.map(direccionDto, Direccion.class);
		Direccion saved = direccionRepository.save(direccion);
		return modelMapper.map(saved, DireccionDto.class);
	}

	@Override
	public DireccionDto findById(Long id) {
		Direccion direccion = direccionRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(direccion, DireccionDto.class);
	}

	@Override
	public List<DireccionDto> findAll() {
		return direccionRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, DireccionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public DireccionDto update(Long id, DireccionDto direccionDto) {
		Direccion direccion = direccionRepository.findById(id)
				.orElseThrow();
		direccion.setAltura(direccionDto.getAltura());
		direccion.setCalle(direccionDto.getCalle());
		Direccion updated = direccionRepository.save(direccion);
		return modelMapper.map(updated, DireccionDto.class);
	}

	@Override
	public void deleteById(Long id) {
		direccionRepository.deleteById(id);
	}

}
