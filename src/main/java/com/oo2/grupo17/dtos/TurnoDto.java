package com.oo2.grupo17.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Getter @Setter
@NoArgsConstructor
public class TurnoDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private ClienteDto cliente;
	private ProfesionalDto profesional;
	private ServicioDto servicio;
	private DisponibilidadDto disponibilidad;
	
}
