package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.entities.Contacto;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.services.IProfesionalService;

@Service
public class ProfesionalService implements IProfesionalService {
	
	private final IProfesionalRepository profesionalRepository;
	private final ModelMapper modelMapper;
	
	public ProfesionalService(IProfesionalRepository profesionalRepository,
							ModelMapper modelMapper) {
		this.profesionalRepository = profesionalRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ProfesionalDto save(ProfesionalDto profesionalDto) {
		Profesional profesional = modelMapper.map(profesionalDto, Profesional.class);
		if(profesional.getContacto() == null) {
			profesional.setContacto(new Contacto());
		}
		Profesional saved = profesionalRepository.save(profesional);
		return modelMapper.map(saved, ProfesionalDto.class);
	}

	@Override
	public ProfesionalDto findById(Long id) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(profesional, ProfesionalDto.class);
	}

	@Override
	public List<ProfesionalDto> findAll() {
		return profesionalRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ProfesionalDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProfesionalDto update(Long id, ProfesionalDto profesionalDto) {
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow();
		profesional.setNombre(profesionalDto.getNombre());
		profesional.setDni(profesionalDto.getDni());
		profesional.setMatricula(profesionalDto.getMatricula());
		Profesional updated = profesionalRepository.save(profesional);
		return modelMapper.map(updated, ProfesionalDto.class);
	}

	@Override
	public void deleteById(Long id) {
		profesionalRepository.deleteById(id);
	}

}
