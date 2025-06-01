package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ServicioDto;

public interface IServicioService {
	
	ServicioDto save(ServicioDto servicioDto);
    
	ServicioDto findById(Long id);
    
    List<ServicioDto> findAll();

    ServicioDto update(Long id, ServicioDto servicioDto);

    void deleteById(Long id);

    List<ServicioDto> findAllByOrderByNombreAsc();
}
