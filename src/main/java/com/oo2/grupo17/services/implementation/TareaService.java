package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.TareaDto;
import com.oo2.grupo17.entities.Tarea;
import com.oo2.grupo17.repositories.ITareaRepository;
import com.oo2.grupo17.services.ITareaService;

@Service
public class TareaService implements ITareaService {

	private final ITareaRepository tareaRepository;
	private final ModelMapper modelMapper;
	
	public TareaService(ITareaRepository tareaRepository, ModelMapper modelMapper) {
		this.tareaRepository = tareaRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TareaDto save(TareaDto tareaDto) {
		Tarea tarea = modelMapper.map(tareaDto, Tarea.class);
		Tarea saved = tareaRepository.save(tarea);
		return modelMapper.map(saved, TareaDto.class);
	}

	@Override
	public TareaDto findById(Long id) {
		Tarea tarea = tareaRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(tarea, TareaDto.class);
	}

	@Override
	public List<TareaDto> findAll() {
		return tareaRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, TareaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TareaDto update(Long id, TareaDto tareaDto) {
		Tarea tarea = tareaRepository.findById(id)
				.orElseThrow();
		tarea.setNombre(tareaDto.getNombre());
		Tarea updated = tareaRepository.save(tarea);
		return modelMapper.map(updated, TareaDto.class);
	}

	@Override
	public void deleteById(Long id) {
		tareaRepository.deleteById(id);
	}
	
}
