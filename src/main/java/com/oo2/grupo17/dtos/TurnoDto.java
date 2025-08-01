package com.oo2.grupo17.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDto {
	
	private Long id;
	private ClienteDto cliente;
	private ProfesionalDto profesional;
	private LugarDto lugar;
	private ServicioDto servicio;
	private DisponibilidadDto disponibilidad;
	
}
