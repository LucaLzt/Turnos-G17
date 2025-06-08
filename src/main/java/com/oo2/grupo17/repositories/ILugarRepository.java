package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Lugar;

@Repository
public interface ILugarRepository extends JpaRepository<Lugar, Long>{
	
	// Ver que poner

	List<Lugar> findByServicios_id(Long servicioId);
}
