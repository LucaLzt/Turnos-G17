package com.oo2.grupo17.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Direccion;

@Repository
public interface IDireccionRepository extends JpaRepository<Direccion, Long> {
	
	// Ver que poner
	
}
