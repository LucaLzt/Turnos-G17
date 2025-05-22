package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.oo2.grupo17.entities.Persona;

@NoRepositoryBean // Indica que es una interfaz base, no un repositorio concreto
public interface IPersonaRepository <T extends Persona> extends JpaRepository<T, Long> {
	
	List<T>findByNombreContainingIgnoreCase(String nombre);
	
	List<T> findByDni(int dni);
	
}
