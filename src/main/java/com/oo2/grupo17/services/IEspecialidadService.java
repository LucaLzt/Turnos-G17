package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.EspecialidadDto;

public interface IEspecialidadService {
	
    EspecialidadDto save(EspecialidadDto especialidadDto);
    
    EspecialidadDto findById(Long id);
    
    List<EspecialidadDto> findAll();

    EspecialidadDto update(Long id, EspecialidadDto especialidadDto);

    void deleteById(Long id);
	
}
