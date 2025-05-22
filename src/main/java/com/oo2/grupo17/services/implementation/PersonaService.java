package com.oo2.grupo17.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.PersonaDto;
import com.oo2.grupo17.entities.Persona;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.repositories.IPersonaRepository;
import com.oo2.grupo17.services.IPersonaService;

@Service("personaService")
public abstract class PersonaService<T extends Persona, D extends PersonaDto> implements IPersonaService<D> {
	
	protected final IPersonaRepository<T> personaRepository;
	protected final ModelMapper modelMapper;
	protected abstract Class<T> getEntityClass();
	protected final Class<D> dtoClass;
	
	public PersonaService(IPersonaRepository<T> personaRepository,
						ModelMapper modelMapper,
						Class<D> dtoClass) {
		this.personaRepository = personaRepository;
		this.modelMapper = modelMapper;
		this.dtoClass = dtoClass;
	}
	
	@Override
	public List<D> findAll() {
		return personaRepository.findAll().stream()
				.map(entity -> modelMapper.map(entity, dtoClass))
				.collect(Collectors.toList());
	}

	@Override
	public D findById(Long id) {
		T entity = personaRepository.findById(id).orElseThrow();
		return modelMapper.map(entity, dtoClass);
	}

	@Override
	public D insertOrUpdate(D dto) {
		 T entity = modelMapper.map(dto, getEntityClass());
		 T savedEntity = personaRepository.save(entity);
		 return modelMapper.map(savedEntity, dtoClass);
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

