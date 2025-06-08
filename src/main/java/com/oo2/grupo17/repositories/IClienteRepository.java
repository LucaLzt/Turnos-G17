package com.oo2.grupo17.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByDni(int dni);
	
	@Query("SELECT c FROM Cliente c WHERE c.contacto.email = :email")
	Optional<Cliente> findByEmail(String email);

	boolean existsByEmail(String email);
	boolean existsByDni(int dni);
	
}
