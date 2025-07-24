package com.oo2.grupo17.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Profesional;

public interface IDisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

	List<Disponibilidad> findByProfesionalIdAndInicioAfterAndOcupadoFalse(Long profesionalId, LocalDateTime fechaActual);
	
	List<Disponibilidad> findByProfesionalIdAndInicioAfter(Long profesionalId, LocalDateTime fechaActual);
	
	boolean existsByProfesionalAndInicio(Profesional profesional, LocalDateTime inicio);

	Optional<Disponibilidad> findByProfesional_IdAndInicio(Long id, LocalDateTime disponibilidad);
	
}
