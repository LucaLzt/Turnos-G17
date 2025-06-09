package com.oo2.grupo17.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "Turno")
public class Turno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public @Setter(AccessLevel.PROTECTED) Long id;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "profesional_id", nullable = false)
	private Profesional profesional;
	
	@ManyToOne
	@JoinColumn(name = "lugar_id", nullable = false)
	private Lugar lugar;
	
	@ManyToOne
	@JoinColumn(name = "servicio_id", nullable = false)
	private Servicio servicio;
	
	@OneToOne
	@JoinColumn(name = "disponibilidad_id", nullable = false)
	private Disponibilidad disponibilidad;
	
}
