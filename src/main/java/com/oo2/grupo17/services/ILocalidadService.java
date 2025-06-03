package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.LocalidadDto;
import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;

public interface ILocalidadService {

    LocalidadDto save(LocalidadDto localidadDto);

    LocalidadDto findById(Long id);
   
    List<Localidad> findAll();
    
    public void deleteById(Long id);
    
    List<Localidad> findByProvincia(Provincia provincia);
    
    public boolean existsByNombreAndProvincia(String nombre, ProvinciaDto provincia);
    
    public void prueba();

	List<Localidad> getLocalidadesPorProvincia(Long idProvincia);
	
}
