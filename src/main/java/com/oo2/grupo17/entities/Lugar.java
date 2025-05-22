package com.oo2.grupo17.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Lugar")
public class Lugar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="direccion", nullable = false)
	private String duracion;
	
	@Column(name="horaApertura", nullable = false)
	private LocalTime horaApertura;
	
	@Column(name="horaCierre", nullable = false)
	private LocalTime horaCierre;
	
	@OneToMany(mappedBy = "lugar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Dia> dias = new ArrayList();
	
	@Column(name="createdat", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name="updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
