package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PersonaDto {
	
	protected @Setter(AccessLevel.PROTECTED) Long id;
	
	private String nombre;
	private int dni;
	private ContactoDto contacto;
	
	public PersonaDto(String nombre, int dni, ContactoDto contacto) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.contacto = contacto;
	}
	
}
