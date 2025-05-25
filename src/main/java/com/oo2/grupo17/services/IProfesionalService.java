package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.ProfesionalDto;

public interface IProfesionalService {
	
    ProfesionalDto save(ProfesionalDto profesionalDto);

    ProfesionalDto findById(Long id);
   
    List<ProfesionalDto> findAll();

    ProfesionalDto update(Long id, ProfesionalDto profesionalDto);

    void deleteById(Long id);
	
}
