package com.oo2.grupo17.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.entities.Profesional;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long>  {


	// Ver que poner
	List<Profesional> findByLugar_id(Long lugarId);

}
