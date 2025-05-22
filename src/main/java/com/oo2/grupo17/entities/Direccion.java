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
	
	// Getters y Setters

	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public Contacto getContacto() {
		return contacto;
	}

	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
}
