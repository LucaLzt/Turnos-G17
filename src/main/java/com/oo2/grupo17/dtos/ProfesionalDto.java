package com.oo2.grupo17.dtos;

import java.util.Set;

import lombok.NoArgsConstructor;

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
	
	// Getters y Setters

	public Integer getMatricula() {
		return matricula;
	}

	protected void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}

	public Set<Long> getTareasHabilitadasIds() {
		return tareasHabilitadasIds;
	}

	public void setTareasHabilitadasIds(Set<Long> tareasHabilitadasIds) {
		this.tareasHabilitadasIds = tareasHabilitadasIds;
	}
	
}
