package com.oo2.grupo17.services;

import java.util.List;
import java.util.Map;

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.entities.Lugar;


public interface ILugarService {
	
    LugarDto save(LugarDto lugarDto);

    LugarDto findById(Long id);
   
    List<LugarDto> findAll();

    LugarDto update(Long id, LugarDto lugarDto);

    void deleteById(Long id);

    List<Lugar> obtenerLugaresPorServicio(Long servicioId);

	List<LugarDto> findAllById(List<Long> lugares);

	Map<Long, Long> getCantidadTurnosPorLugar();

	List<LugarDto> findByCalle(String nombre);

	LugarDto findByCalleAndAltura(String direccion, int altura);
}
