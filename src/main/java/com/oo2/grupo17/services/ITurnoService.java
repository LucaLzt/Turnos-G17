package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Turno;

public interface ITurnoService {
	
	TurnoDto save(TurnoDto turnoDto);
    
	TurnoDto findById(Long id);
    
    List<TurnoDto> findAll();

    void deleteById(Long id);
    
    List<Turno> buscarTurnosPorClienteId(Long clienteId);
    
    void eliminarTurno(Long id);
	
}
