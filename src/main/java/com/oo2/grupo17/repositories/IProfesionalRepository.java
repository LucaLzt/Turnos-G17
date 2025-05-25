package com.oo2.grupo17.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Profesional;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long>  {

	// Ver que poner
	
}
