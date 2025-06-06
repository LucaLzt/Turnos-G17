package com.oo2.grupo17.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("PROFESIONAL")
public class Profesional extends Persona {
	
	@Column(name="matricula", nullable = true)
	private Integer matricula;
	
	@ManyToOne
	@JoinColumn(name = "especialidad_id", nullable = true)
	private Especialidad especialidad;
	
	@OneToMany(mappedBy="profesional", cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<Disponibilidad> disponibilidades = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "lugar_id", nullable = true)
	private Lugar lugar;
	
	@ManyToMany(mappedBy = "profesionales")
	private Set<Servicio> servicios = new HashSet<>();
	
	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Turno> lstTurnos = new HashSet<>();

}
