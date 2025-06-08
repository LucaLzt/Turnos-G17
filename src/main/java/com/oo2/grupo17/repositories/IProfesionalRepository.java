package com.oo2.grupo17.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Profesional;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long>  {

	@Query("SELECT p FROM Profesional p WHERE p.contacto.email = :email")
	Optional<Profesional> findByEmail(String email);
	
	boolean existsByEmail(String email);
	boolean existsByDni(int dni);
	
}
