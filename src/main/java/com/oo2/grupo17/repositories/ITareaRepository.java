package com.oo2.grupo17.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Tarea;

@Repository
public interface ITareaRepository extends JpaRepository<Tarea, Long>  {
	
	// Ver que poner
	Optional<Tarea> findByNombre(String nombre);
	Optional<Tarea> findByNombreIgnoreCase(String nombre);
	
}
