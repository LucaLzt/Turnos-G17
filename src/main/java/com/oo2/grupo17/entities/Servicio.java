package com.oo2.grupo17.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "Servicio")
public class Servicio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "precio")
	private Double precio;
	
	@ManyToMany
	@JoinTable(name = "serviciosProfesionales",
			joinColumns = @JoinColumn(name = "servicio_id"),
			inverseJoinColumns = @JoinColumn(name = "profesional_id"))
	private Set<Profesional> profesionales = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "serviciosLugares",
			joinColumns = @JoinColumn(name = "servicio_id"),
			inverseJoinColumns = @JoinColumn(name = "lugar_id"))
	private Set<Lugar> lugares = new HashSet<>();

}