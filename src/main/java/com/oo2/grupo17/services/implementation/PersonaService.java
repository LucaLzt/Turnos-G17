package com.oo2.grupo17.services.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.PersonaDto;
import com.oo2.grupo17.entities.Persona;
import com.oo2.grupo17.repositories.IPersonaRepository;
import com.oo2.grupo17.services.IPersonaService;

@Service("personaService")
public class PersonaService implements IPersonaService {
	
	@Autowired
	@Qualifier("personaRepository")
	private IPersonaRepository<Persona> personaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<Persona> getAll() {
		return personaRepository.findAll();
	}
	
	@Override
	public PersonaDto insertOrUpdate(PersonaDto personaDto) {
		Persona persona = modelMapper.map(personaDto, Persona.class);
		Persona savedPersona = personaRepository.save(persona);
		return modelMapper.map(savedPersona, PersonaDto.class);
	}
	
	@Override
	public boolean remove(Long id) {
		try {
			personaRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

