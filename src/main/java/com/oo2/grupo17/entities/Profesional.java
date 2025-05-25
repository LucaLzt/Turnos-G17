package com.oo2.grupo17.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("PROFESIONAL")
public class Profesional extends Persona {
	
	@Column(name="matricula", nullable = true)
	private Integer matricula;
	
	// Hacer la entidad Turno antes de descomentar esto (Fijarse si la relacion esta bien)
	/*
	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
	private Set<Turno> lstTurnos = new HashSet<>();
	*/
	
	@ManyToMany
	@JoinTable(name = "profesionalEspecialidad", 
			joinColumns = @JoinColumn(name = "profesional_id"), 
			inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
	private Set<Especialidad> especialidadesHabilitadas = new HashSet<>();

}
