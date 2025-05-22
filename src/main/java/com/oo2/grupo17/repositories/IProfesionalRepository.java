package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oo2.grupo17.entities.Profesional;

public interface IProfesionalRepository extends IPersonaRepository<Profesional> {

	public abstract Optional<Profesional> findByMatricula(int matricula);
	
	@Query("SELECT p FROM Profesional p JOIN p.tareasHabilitadas t WHERE t.nombre = :nombreTarea")
	public abstract List<Profesional> findByTareaHabilitada(@Param("nombreTarea") String nombreTarea);
	
}
