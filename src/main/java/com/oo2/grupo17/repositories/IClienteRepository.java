package com.oo2.grupo17.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    // Ver que poner
	Optional<Cliente> findByDni(int dni);
	
}
