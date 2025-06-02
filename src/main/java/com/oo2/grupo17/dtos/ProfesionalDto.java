package com.oo2.grupo17.dtos;

import java.util.Set;

import com.oo2.grupo17.entities.Contacto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalDto extends PersonaDto {
	
	private Integer matricula;
	private EspecialidadDto especialidad;
	private Set<ServicioDto> servicios;
	private LugarDto lugar;
	// private Set<TurnoDto> turnos;  <-- Crear el TurnoDto antes de descomentar esto (Se usa TurnoDto porque se necesitan detalles)
	
	// Agregar Set<TurnoDto> cuando se construya
	public ProfesionalDto(String nombre, int dni, Contacto contacto, Integer matricula,
			EspecialidadDto especialidad) {
		super(nombre, dni, contacto);
		this.matricula = matricula;
		this.especialidad = especialidad;
	}
	
}
