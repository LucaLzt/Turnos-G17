package com.oo2.grupo17.services.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.LocalidadDto;
import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.repositories.ILocalidadRepository;
import com.oo2.grupo17.services.ILocalidadService;

import lombok.Builder;

@Service @Builder
public class LocalidadService implements ILocalidadService{
	
	private final ILocalidadRepository localidadRepository;
	private final ModelMapper modelMapper;

	@Override
	public LocalidadDto save(LocalidadDto localidadDto) {
		Localidad localidad = modelMapper.map(localidadDto, Localidad.class);
		Localidad saved = localidadRepository.save(localidad);
		return modelMapper.map(saved, LocalidadDto.class);
	}

	@Override
	public LocalidadDto findById(Long id) {
		Localidad localidad = localidadRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(localidad, LocalidadDto.class);
	}

	@Override
	public List<Localidad> findAll() {
		return localidadRepository.findAllByOrderByNombreAsc()
				.stream()
				.toList();
	}

	@Override
	public void deleteById(Long id) {
		localidadRepository.deleteById(id);
	}

	@Override
	public List<Localidad> findByProvincia(Provincia provincia) {
		return localidadRepository.findByProvincia(provincia);
	}

	@Override
	public boolean existsByNombreAndProvincia(String nombre, ProvinciaDto provincia) {
		return localidadRepository.existsByNombreIgnoreCaseAndProvincia(nombre, provincia);
	}
	
	@Override
	public void prueba() {
		Localidad localidad = localidadRepository.findAll().get(0);
		System.out.println("Entidad: id=" + localidad.getId() + ", nombre=" + localidad.getNombre());
		LocalidadDto dto = modelMapper.map(localidad, LocalidadDto.class);
		System.out.println("DTO: id=" + dto.getId() + ", nombre=" + dto.getNombre());
	}
	
}
