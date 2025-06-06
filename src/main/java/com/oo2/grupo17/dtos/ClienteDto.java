package com.oo2.grupo17.dtos;

import java.util.Set;

import com.oo2.grupo17.entities.Contacto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ClienteDto extends PersonaDto {
	
	private String nroCliente;
	private Set<Long> turnosIds; // <-- Se usa ids para tener referencia del turno (No tanto detalle)
	
	public ClienteDto(String nombre, int dni, Contacto contacto, String nroCliente, Set<Long> turnosIds) {
		super(nombre, dni, contacto);
		this.nroCliente = nroCliente;
		this.turnosIds = turnosIds;
	}
	
}
