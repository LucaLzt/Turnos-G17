package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LugarDto {
	
	private Long id;
	private DireccionDto direccion;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	
}
