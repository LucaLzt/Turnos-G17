package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oo2.grupo17.entities.Cliente;
import com.oo2.grupo17.entities.Profesional;

public interface IClienteRepository extends IPersonaRepository<Cliente> {

	Optional<Cliente> findByNroCliente(int nroCliente);
	
	/*
	@Query("SELECT c FROM Cliente c WHERE SIZE (c.lstTurnos) > :minTurnos")
	List<Cliente> findByClientesFrecuentes(@Param("minTurnos") int minTurnos);
	*/
	
}
