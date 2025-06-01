package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.LugarDto;

public interface IDireccionService {
	
	DireccionDto save(DireccionDto direccionDto);
    
	DireccionDto findById(Long id);
    
    List<DireccionDto> findAll();

    DireccionDto update(Long id, DireccionDto direccionDto);

    void deleteById(Long id);
    
    DireccionDto findByContactoEmail(String email);
    
    DireccionDto crearDireccion(ContactoDto contacto, DireccionDto direccionDto);
    
    DireccionDto actualizarDireccion(ContactoDto contactoDto, DireccionDto direccionDto);

	DireccionDto crearDireccion(LugarDto lugarDto, DireccionDto direccionDto);
	
	DireccionDto actualizarDireccion(LugarDto lugarDto, DireccionDto direccionDto);
	
}
