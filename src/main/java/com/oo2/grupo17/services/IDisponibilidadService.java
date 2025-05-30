package com.oo2.grupo17.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.oo2.grupo17.dtos.DisponibilidadDto;

public interface IDisponibilidadService {
	
	DisponibilidadDto save(DisponibilidadDto disponbilidadDto);
    
	DisponibilidadDto findById(Long id);
    
    List<DisponibilidadDto> findAll();

    DisponibilidadDto updateOcupacion(Long id, DisponibilidadDto disponbilidadDto);

    void deleteById(Long id);
    
    public void generarDisponibilidadesAutomaticas(Long profesionalId, LocalTime horaIncio, LocalTime horaFin,
    		Duration duracionTurno, LocalDate fechaInicio, LocalDate fechaFin);
    
}
