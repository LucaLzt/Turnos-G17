package com.oo2.grupo17.dtos;

import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClienteDto extends PersonaDto {
	
	private Integer nroCliente;
	private Set<Long> turnosIds; // <-- Se usa ids para tener referencia del turno (No tanto detalle)
	
	public ClienteDto(String nombre, int dni, ContactoDto contacto, Integer nroCliente, Set<Long> turnosIds) {
		super(nombre, dni, contacto);
		this.nroCliente = nroCliente;
		this.turnosIds = turnosIds;
	}

	// Getters y Setters
	
	public Integer getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(Integer nroCliente) {
		this.nroCliente = nroCliente;
	}

	public Set<Long> getTurnosIds() {
		return turnosIds;
	}

	public void setTurnosIds(Set<Long> turnosIds) {
		this.turnosIds = turnosIds;
	}
	
}
