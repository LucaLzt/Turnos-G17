package com.oo2.grupo17.repositories;

import java.util.List;

import com.oo2.grupo17.entities.Profesional;

public interface IProfesionalRepository extends IPersonaRepository<Profesional> {

	List<Profesional> findByTareasHabilitadas_Nombre(String nombreTarea);
	
}
