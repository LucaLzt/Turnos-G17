package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.TurnoDto;

public interface ITurnoService {
	
	TurnoDto save(TurnoDto turnoDto);
    
	TurnoDto findById(Long id);
	
	TurnoDto update(Long id, TurnoDto turnoDto);
    
    List<TurnoDto> findAll();

    void deleteById(Long id);
    
	void crearTurno(TurnoDto turnoDto);
    
    List<TurnoDto> buscarTurnosPorClienteId(Long clienteId);
    
    List<TurnoDto> buscarTurnosPorProfesionalId(Long profesionalId);

	TurnoDto findByIdDisponibilidad(Long id);

	boolean reprogramarTurno(long id, long nuevaDisponibilidad);

	boolean existsByServicio(Long id);
	
}
