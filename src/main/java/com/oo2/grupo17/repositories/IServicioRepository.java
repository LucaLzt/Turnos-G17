package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Servicio;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Long>{
	
	// Ver que poner
	List<Servicio> findAllByOrderByNombreAsc();
	
	List<Servicio> findAllByLugares_Id(Long id);
}
