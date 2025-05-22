package com.oo2.grupo17.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.DiaDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Turno")
public class Turno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="cliente", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="profesional", nullable = false)
	private Profesional profesional;
	
	@ManyToOne
	@JoinColumn(name="servicio", nullable = false)
	private Servicio servicio;
	
	@ManyToOne
	@JoinColumn(name="dia", nullable = false)
	private Dia dia;
	
	@Column(name="hora", nullable = false)
	private LocalTime hora;
	
	@Column(name="createdat", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name="updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
