package com.oo2.grupo17.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo17.entities.Disponibilidad;

public interface IDisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

	List<Disponibilidad> findByProfesionalIdAndInicioAfter(Long profesionalId, LocalDateTime fechaActual);
	
}
