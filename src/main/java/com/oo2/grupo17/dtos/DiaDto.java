package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DiaDto {
	
	protected @Setter(AccessLevel.PROTECTED) Long id;
	
	private LocalTime fecha;
	private LugarDto lugar;
	
	public DiaDto(LocalTime fecha, LugarDto lugar) {
		super();
		this.fecha = fecha;
		this.lugar = lugar;
	}
	
	
}
