package com.oo2.grupo17.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Direccion")
public class Direccion {
	
	@Id
	private Long id;
	
	@Column(name="calle", nullable = false)
	private String calle;
	
	@Column(name="altura", nullable = false)
	private int altura;
	
	@Column(name="localidad", nullable = false)
	private String localidad;
	
	@Column(name="provincia", nullable = false)
	private String provincia;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="id")
	private Contacto contacto;
	
}
