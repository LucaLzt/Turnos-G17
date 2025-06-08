package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.services.IEspecialidadService;

import lombok.Builder;

@Service @Builder
public class EspecialidadService implements IEspecialidadService {

	private final IEspecialidadRepository especialidadRepository;
	private final IProfesionalRepository profesionalRepository;
	private final ModelMapper modelMapper;

	@Override
	public EspecialidadDto save(EspecialidadDto especialidadDto) {
		Especialidad espe = modelMapper.map(especialidadDto, Especialidad.class);
		Especialidad saved = especialidadRepository.save(espe);
		return modelMapper.map(saved, EspecialidadDto.class);
	}

	@Override
	public EspecialidadDto findById(Long id) {
		Especialidad espe = especialidadRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(espe, EspecialidadDto.class);
	}

	@Override
	public List<EspecialidadDto> findAll() {
		return especialidadRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, EspecialidadDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public EspecialidadDto update(Long id, EspecialidadDto especialidadDto) {
		Especialidad espe = especialidadRepository.findById(id)
				.orElseThrow();
		espe.setNombre(especialidadDto.getNombre());
		Especialidad updated = especialidadRepository.save(espe);
		return modelMapper.map(updated, EspecialidadDto.class);
	}

	@Override
	public void deleteById(Long id) {
		Especialidad especialidad = especialidadRepository.findById(id).orElseThrow();
		
		Set<Profesional> profesionales = especialidad.getProfesionales();
		
		for(Profesional p : profesionales) {
			p.setEspecialidad(null);
			profesionalRepository.save(p);
		}
		especialidad.setProfesionales(null);
		
		especialidadRepository.delete(especialidad);
	}
	
}
