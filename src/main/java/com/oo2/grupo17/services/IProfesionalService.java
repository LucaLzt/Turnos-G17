package com.oo2.grupo17.services;

import java.util.Set;

import com.oo2.grupo17.dtos.ProfesionalDto;

public interface IProfesionalService extends IPersonaService<ProfesionalDto>{
	
	Set<ProfesionalDto> findByTareaHabilitada(String nombreTarea);
	
	void asignarTareas(Long idProfesional, Set<Long> tareaIds);
	
}
