package com.oo2.grupo17.services;

import java.util.List;
import java.util.Set;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Servicio;

public interface IServicioService {
	
	ServicioDto save(ServicioDto servicioDto);
    
	ServicioDto findById(Long id);
    
    List<ServicioDto> findAll();

    ServicioDto update(Long id, ServicioDto servicioDto);

    void deleteById(Long id);

    List<ServicioDto> findAllByOrderByNombreAsc();
    
    List<Servicio> traerServiciosPorLugar(Long lugarId);

	List<Servicio> findAllByIds(Set<Long> todosLosServiciosIds);
}
