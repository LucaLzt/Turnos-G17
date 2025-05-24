package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ContactoDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private String email;
	private int movil;
	private int telefono;
	private DireccionDto direccion;

	public ContactoDto(String email, int movil, int telefono, DireccionDto direccion) {
		super();
		this.email = email;
		this.movil = movil;
		this.telefono = telefono;
		this.direccion = direccion;
	}
	
}
