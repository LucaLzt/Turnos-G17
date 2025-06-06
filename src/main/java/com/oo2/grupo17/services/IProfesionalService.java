package com.oo2.grupo17.services;

import java.util.List;
import java.util.Set;

import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;

public interface IProfesionalService {
	
    ProfesionalDto save(ProfesionalDto profesionalDto);
    
    ProfesionalDto findById(Long id);
    
    List<ProfesionalDto> findAll();
    
    ProfesionalDto update(Long id, ProfesionalDto profesionalDto);
    
    void deleteById(Long id);
    
    public void registrarProfesional(ProfesionalRegistradoDto registroDto);
    
    void asignarDatosProfesional(Long id, Long especialidadId, Long lugarId, Set<Long> serviciosId);
}
