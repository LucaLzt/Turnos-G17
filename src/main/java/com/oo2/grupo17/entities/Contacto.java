package com.oo2.grupo17.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Contacto")
public class Contacto {
	
	@Id
	private Long id;
	
	@Column(name="email")
	private String email;
	
	@Column(name="movil")
	private int movil;
	
	@Column(name="telefono")
	private int telefono;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="id")
	private Persona persona;
	
	@OneToOne(mappedBy = "contacto", cascade = CascadeType.ALL, orphanRemoval = true)
	private Direccion direccion;
	
	@Column(name="createdat")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name="updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
