package com.oo2.grupo17.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Persona{
	
	@Column(name="nroCliente", nullable = false)
	private Integer nroCliente;
	
	// Hacer la entidad Turno antes de descomentar esto (Fijarse si la relacion esta bien)
	/*
	@OneToMany(mappedBy = "cliente")
	private Set<Turno> lstTurnos = new HashSet<>();
	*/
	
	// Getters y Setters
	
	public Integer getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(Integer nroCliente) {
		this.nroCliente = nroCliente;
	}
	
}
