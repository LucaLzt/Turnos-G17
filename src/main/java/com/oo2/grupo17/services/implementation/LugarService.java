package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.repositories.ILugarRepository;
import com.oo2.grupo17.services.ILugarService;

import lombok.Builder;

@Service @Builder
public class LugarService implements ILugarService {
	
	private final ILugarRepository lugarRepository;
	private final ModelMapper modelMapper;

	@Override
	public LugarDto save(LugarDto lugarDto) {
		Lugar lugar = modelMapper.map(lugarDto, Lugar.class);
		Lugar saved = lugarRepository.save(lugar);
		return modelMapper.map(saved, LugarDto.class);
	}

	@Override
	public LugarDto findById(Long id) {
		Lugar lugar = lugarRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(lugar, LugarDto.class);
	}

	@Override
	public List<LugarDto> findAll() {
		return lugarRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, LugarDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public LugarDto update(Long id, LugarDto lugarDto) {
		Lugar lugar = lugarRepository.findById(id)
				.orElseThrow();
		lugar.setHorarioApertura(lugarDto.getHorarioApertura());
		lugar.setHorarioCierre(lugarDto.getHorarioCierre());
		// lugar.setDireccion(lugarDto.getDireccion());
		Lugar updated = lugarRepository.save(lugar);
		return modelMapper.map(updated, LugarDto.class);
	}

	@Override
	public void deleteById(Long id) {
		lugarRepository.deleteById(id);
	}
	
	@Override
	public List<Lugar> obtenerLugaresPorServicio(Long servicioId) {
		return lugarRepository.findByServicios_id(servicioId);
	}
	
	@Override
	public List<LugarDto> findAllById(List<Long> lugares) {
		return lugarRepository.findAllById(lugares)
				.stream()
				.map(object -> modelMapper.map(object, LugarDto.class))
				.collect(Collectors.toList());
	};

}
