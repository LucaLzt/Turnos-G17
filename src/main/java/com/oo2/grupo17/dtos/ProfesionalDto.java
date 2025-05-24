package com.oo2.grupo17.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalDto extends PersonaDto{
	
	private Integer matricula;
	private Set<Long> tareasHabilitadasIds;
	// private Set<TurnoDto> turnos;  <-- Crear el TurnoDto antes de descomentar esto (Se usa TurnoDto porque se necesitan detalles)
	
	// Agregar Set<TurnoDto> cuando se construya
	public ProfesionalDto(String nombre, int dni, ContactoDto contacto, Integer matricula,
			Set<Long> tareasHabilitadasIds) {
		super(nombre, dni, contacto);
		this.matricula = matricula;
		this.tareasHabilitadasIds = tareasHabilitadasIds;
	}
	
}
