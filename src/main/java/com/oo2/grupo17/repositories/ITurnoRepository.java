package com.oo2.grupo17.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo17.entities.Disponibilidad;
import com.oo2.grupo17.entities.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
	
	// Ver que poner
	int countByClienteId(Long clienteId);
	List<Turno> findByClienteId(Long clienteId);
	List<Turno> findByProfesionalId(Long profesionalId);
	Optional<Turno> findByDisponibilidad(Disponibilidad disponibilidad);
	
	@Query("SELECT t.lugar.id, COUNT(t) FROM Turno t GROUP BY t.lugar.id")
	List<Object[]> countTurnosGroupByLugar();
	
	boolean existsByIdAndClienteId(Long turnoId, Long id);
	
	boolean existsByIdAndProfesionalId(Long turnoId, Long profesionalId);
	
}
