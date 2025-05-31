package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.DireccionDto;

public interface IDireccionService {
	
	DireccionDto save(DireccionDto direccionDto);
    
	DireccionDto findById(Long id);
    
    List<DireccionDto> findAll();

    DireccionDto update(Long id, DireccionDto direccionDto);

    void deleteById(Long id);
    
    public DireccionDto findByContactoEmail(String email);
	
}
