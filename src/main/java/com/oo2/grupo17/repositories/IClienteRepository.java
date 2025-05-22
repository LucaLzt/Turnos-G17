package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oo2.grupo17.entities.Cliente;

public interface IClienteRepository extends IPersonaRepository<Cliente> {

	public abstract Optional<Cliente> findByNroCliente(int nroCliente);
	
	@Query("SELECT c FROM Cliente c WHERE SIZE (c.lstTurnos) > :minTurnos")
	public abstract List<Cliente> findByClientesFrecuentes(@Param("minTurnos") int minTurnos);
	
}
