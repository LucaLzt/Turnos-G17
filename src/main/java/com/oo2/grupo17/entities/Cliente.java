package com.oo2.grupo17.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("CLIENTE")
public class Cliente extends Persona{
	
	@Column(name="nroCliente", nullable = true)
	private String nroCliente;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Turno> lstTurnos = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
}
