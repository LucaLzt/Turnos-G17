package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Especialidad;

@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Long>  {
	
	// Ver que poner
	Optional<Especialidad> findByNombre(String nombre);
	Optional<Especialidad> findByNombreIgnoreCase(String nombre);
	List<Especialidad> findAllByOrderByNombreAsc();
	boolean existsByNombre(String nombre);
	
}
