package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.repositories.ITurnoRepository;
import com.oo2.grupo17.services.ITurnoService;

public class TurnoService implements ITurnoService {
	
	private final ITurnoRepository turnoRepository;
	private final ModelMapper modelMapper;
	
	public TurnoService(ITurnoRepository turnoRepository, ModelMapper modelMapper) {
		this.turnoRepository = turnoRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TurnoDto save(TurnoDto turnoDto) {
		Turno turno = modelMapper.map(turnoDto, Turno.class);
		Turno saved = turnoRepository.save(turno);
		return modelMapper.map(saved, TurnoDto.class);
	}

	@Override
	public TurnoDto findById(Long id) {
		Turno turno = turnoRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(turno, TurnoDto.class);
	}

	@Override
	public List<TurnoDto> findAll() {
		return turnoRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, TurnoDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {
		turnoRepository.deleteById(id);
	}

}
