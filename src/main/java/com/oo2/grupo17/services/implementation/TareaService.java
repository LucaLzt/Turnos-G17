package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.TareaDto;
import com.oo2.grupo17.entities.Tarea;
import com.oo2.grupo17.repositories.ITareaRepository;
import com.oo2.grupo17.services.ITareaService;

@Service("tareaService")
public class TareaService implements ITareaService {

	private final ITareaRepository tareaRepository;
	private final ModelMapper modelMapper;
	
	public TareaService(ITareaRepository tareaRepository, ModelMapper modelMapper) {
		this.tareaRepository = tareaRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<TareaDto> findAll() {
		return tareaRepository.findAll().stream()
				.map(entity -> modelMapper.map(entity, TareaDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TareaDto findById(Long id) {
		Tarea entity = tareaRepository.findById(id).orElseThrow();
		return modelMapper.map(entity, TareaDto.class);
	}

	@Override
	public TareaDto insertOrUpdate(TareaDto dto) {
		Tarea entity = modelMapper.map(dto, Tarea.class);
		Tarea saved = tareaRepository.save(entity);
		return modelMapper.map(saved, TareaDto.class);
	}

	@Override
	public boolean remove(Long id) {
		try {
			tareaRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
