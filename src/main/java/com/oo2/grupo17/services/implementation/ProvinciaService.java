package com.oo2.grupo17.services.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.repositories.IProvinciaRepository;
import com.oo2.grupo17.services.IProvinciaService;

import lombok.Builder;

@Service @Builder
public class ProvinciaService implements IProvinciaService{
	
	private final IProvinciaRepository provinciaRepository;
	private final ModelMapper modelMapper;

	@Override
	public ProvinciaDto save(ProvinciaDto provinciaDto) {
		Provincia provincia = modelMapper.map(provinciaDto, Provincia.class);
		Provincia saved = provinciaRepository.save(provincia);
		return modelMapper.map(saved, ProvinciaDto.class);
	}

	@Override
	public ProvinciaDto findById(Long id) {
		Provincia provincia = provinciaRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(provincia, ProvinciaDto.class);
	}

	@Override
	public List<Provincia> findAll() {
		return provinciaRepository.findAllByOrderByNombreAsc()
				.stream()
				.toList();
	}

	@Override
	public void deleteById(Long id) {
		provinciaRepository.deleteById(id);
	}
	
}
