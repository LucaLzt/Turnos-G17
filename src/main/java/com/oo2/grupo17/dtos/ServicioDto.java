package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ServicioDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private String nombre;
	private String descripcion;
	private Double precio;
	
}
