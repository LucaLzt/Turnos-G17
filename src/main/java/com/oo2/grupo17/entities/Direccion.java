package com.oo2.grupo17.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "Direccion")
public class Direccion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	@Column(name = "calle")
	private String calle;
	
	@Column(name = "altura")
	private int altura;
	
	@ManyToOne
	@JoinColumn(name="localidad_id")
	private Localidad localidad;
	
	@ManyToOne
	@JoinColumn(name="provincia_id")
	private Provincia provincia;
	
}
