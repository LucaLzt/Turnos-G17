package com.oo2.grupo17.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Lugar;

@Repository
public interface ILugarRepository extends JpaRepository<Lugar, Long>{
	
	// Ver que poner
	@Query("SELECT l FROM Lugar l JOIN l.servicios s WHERE s.id = :servicioId")
	Set<Lugar> findByServicioId(@Param("servicioId") Long servicioId);
	
}
