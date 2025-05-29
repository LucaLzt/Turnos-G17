package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import com.oo2.grupo17.entities.Direccion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LugarDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private Direccion direccion;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	
}
