package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LugarDto {
	
	protected @Setter(AccessLevel.PROTECTED) Long id;
	
	private String direccion;
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	
	public LugarDto(String direccion, LocalTime horaApertura, LocalTime horaCierre) {
		super();
		this.direccion = direccion;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
	}
	
	
}
