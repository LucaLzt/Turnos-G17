package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.entities.Especialidad;
import com.oo2.grupo17.repositories.IEspecialidadRepository;
import com.oo2.grupo17.services.IEspecialidadService;

@Service
public class EspecialidadService implements IEspecialidadService {

	private final IEspecialidadRepository especialidadRepository;
	private final ModelMapper modelMapper;
	
	public EspecialidadService(IEspecialidadRepository especialidadRepository, ModelMapper modelMapper) {
		this.especialidadRepository = especialidadRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public EspecialidadDto save(EspecialidadDto tareaDto) {
		Especialidad tarea = modelMapper.map(tareaDto, Especialidad.class);
		Especialidad saved = especialidadRepository.save(tarea);
		return modelMapper.map(saved, EspecialidadDto.class);
	}

	@Override
	public EspecialidadDto findById(Long id) {
		Especialidad tarea = especialidadRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(tarea, EspecialidadDto.class);
	}

	@Override
	public List<EspecialidadDto> findAll() {
		return especialidadRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, EspecialidadDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public EspecialidadDto update(Long id, EspecialidadDto tareaDto) {
		Especialidad tarea = especialidadRepository.findById(id)
				.orElseThrow();
		tarea.setNombre(tareaDto.getNombre());
		Especialidad updated = especialidadRepository.save(tarea);
		return modelMapper.map(updated, EspecialidadDto.class);
	}

	@Override
	public void deleteById(Long id) {
		especialidadRepository.deleteById(id);
	}
	
}
