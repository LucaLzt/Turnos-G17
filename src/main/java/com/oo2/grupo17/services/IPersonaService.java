package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.PersonaDto;
import com.oo2.grupo17.entities.Persona;

public interface IPersonaService {
	
	public abstract List<Persona> getAll();
	
	public abstract PersonaDto insertOrUpdate(PersonaDto personaModel);
	
	public abstract boolean remove(Long id);

}
