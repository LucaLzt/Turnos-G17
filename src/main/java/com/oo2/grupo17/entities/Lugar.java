package com.oo2.grupo17.entities;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "Lugar")
public class Lugar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	private Direccion direccion;
	
	@Column(name = "horario_apertura", nullable = false)
	private LocalTime horarioApertura;
	
	@Column(name = "horario_cierre", nullable = false)
	private LocalTime horarioCierre;
	
	@OneToMany(mappedBy = "lugar", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Profesional> profesionales = new HashSet<>();
	
	@ManyToMany(mappedBy = "lugaresServicios")
	private Set<Servicio> servicios = new HashSet<>();
	
}
