package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.LugarDto;


public interface ILugarService {
	
    LugarDto save(LugarDto lugarDto);

    LugarDto findById(Long id);
   
    List<LugarDto> findAll();

    LugarDto update(Long id, LugarDto lugarDto);

    void deleteById(Long id);

}
