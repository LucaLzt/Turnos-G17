package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.TareaDto;

public interface ITareaService {
	
	List<TareaDto> findAll();
	
	TareaDto findById(Long id);
	
	TareaDto insertOrUpdate(TareaDto dto);
	
	boolean remove(Long id);
	
}
