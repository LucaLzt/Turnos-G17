package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.oo2.grupo17.entities.Persona;

@NoRepositoryBean // Indica que es una interfaz base, no un repositorio concreto
public interface IPersonaRepository <T extends Persona> extends JpaRepository<T, Long>{
	
	Class<T> getEntityClass();
	
	List<T>findByNombreContainingIgnoreCase(String nombre);
	
	List<T> findByDni(int dni);
	
	@Query("SELECT p FROM #{#EntityName} p WHERE p.contacto.email = :email")
	List<T> findByEmailContacto(@Param("email") String email);
	
}
