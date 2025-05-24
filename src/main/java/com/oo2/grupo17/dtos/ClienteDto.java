package com.oo2.grupo17.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ClienteDto extends PersonaDto {
	
	private Integer nroCliente;
	private Set<Long> turnosIds; // <-- Se usa ids para tener referencia del turno (No tanto detalle)
	
	public ClienteDto(String nombre, int dni, ContactoDto contacto, Integer nroCliente, Set<Long> turnosIds) {
		super(nombre, dni, contacto);
		this.nroCliente = nroCliente;
		this.turnosIds = turnosIds;
	}
	
}
