package com.oo2.grupo17.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Provincia;

@Repository
public interface IProvinciaRepository extends JpaRepository<Provincia, Long>{
	
	List<Provincia> findAllByOrderByNombreAsc();
	
}
