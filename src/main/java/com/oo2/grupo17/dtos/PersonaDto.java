package com.oo2.grupo17.dtos;

import com.oo2.grupo17.entities.Contacto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PersonaDto {
	
	protected Long id;
	private String nombre;
	private int dni;
	private Contacto contacto;

	public PersonaDto(String nombre, int dni, Contacto contacto) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.contacto = contacto;
	}
	
}
