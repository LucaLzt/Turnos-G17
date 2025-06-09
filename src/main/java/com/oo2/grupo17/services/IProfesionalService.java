package com.oo2.grupo17.services;

import java.util.List;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.GenerarDisponibilidadDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.entities.Profesional;

public interface IProfesionalService {
	
    ProfesionalDto save(ProfesionalDto profesionalDto);
    
    ProfesionalDto findById(Long id);
    
    List<ProfesionalDto> findAll();
    
    List<Profesional> obtenerProfesionalesPorLugar(Long lugarId);
    
    ProfesionalDto update(Long id, ProfesionalDto profesionalDto);
    
    void deleteById(Long id);
    
    void eliminarProfesional(Long id);
    
    public void registrarProfesional(ProfesionalRegistradoDto registroDto);

	ProfesionalDto findByEmail(String email);

	void updatearContactoUserEntity(ContactoDto contactoDto);

	void generarDisponibilidadesAutomaticas(GenerarDisponibilidadDto dto);
    
	void asignarDatosProfesional(Long id, ProfesionalDto profesionalDto, List<Long> serviciosIds);

	void cambiarContrasena(ProfesionalDto profesional, CambioPasswordDto cambioPasswordDto);
	
}
