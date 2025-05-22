package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TurnoDto {
	
	protected @Setter(AccessLevel.PROTECTED) Long id;
	
	private ClienteDto cliente;
	private ProfesionalDto profesional;
	private ServicioDto servicio;
	private DiaDto dia;
	
	private LocalTime hora;

	public TurnoDto(ClienteDto cliente, ProfesionalDto profesional, ServicioDto servicio, DiaDto dia, LocalTime hora) {
		super();
		this.cliente = cliente;
		this.profesional = profesional;
		this.servicio = servicio;
		this.dia = dia;
		this.hora = hora;
	}
	
	
}
