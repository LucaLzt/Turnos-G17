package com.oo2.grupo17.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Contacto;

@Repository
public interface IContactoRepository extends JpaRepository<Contacto, Long>{
	
	// Ver que poner
	
}
