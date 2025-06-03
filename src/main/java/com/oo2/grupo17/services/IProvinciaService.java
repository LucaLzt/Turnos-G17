package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Provincia;

public interface IProvinciaService {
	
    ProvinciaDto save(ProvinciaDto clienteDto);

    ProvinciaDto findById(Long id);
   
    List<Provincia> findAll();
    
    public void deleteById(Long id);

}
