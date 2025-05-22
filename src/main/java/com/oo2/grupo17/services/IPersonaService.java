package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.PersonaDto;
import com.oo2.grupo17.entities.Profesional;

public interface IPersonaService<T extends PersonaDto>{
	
	List<T> findAll();
	
	T findById(Long id);
	
	T insertOrUpdate(T dto);
	
	boolean remove(Long id);

}
