package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.TareaDto;

public interface ITareaService {
	
    TareaDto save(TareaDto tareaDto);

    TareaDto findById(Long id);
   
    List<TareaDto> findAll();

    TareaDto update(Long id, TareaDto tareaDto);

    void deleteById(Long id);
	
}
