package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
	
	// Ver que poner
	int countByClienteId(Long clienteId);
	List<Turno> findByClienteId(Long clienteId);
	List<Turno> findByProfesionalId(Long profesionalId);
	
	
}
