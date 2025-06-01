package com.oo2.grupo17.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDto {
	
	private Long id;
	private String email;
	private int movil;
	private int telefono;
	private DireccionDto direccion;
	
}
