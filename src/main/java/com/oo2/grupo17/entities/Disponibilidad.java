package com.oo2.grupo17.entities;

import java.time.Duration;
import java.time.LocalDateTime;

import com.oo2.grupo17.converters.DurationConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="Disponibilidad")
public class Disponibilidad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	@Column(name = "inicio", nullable = true)
	private LocalDateTime inicio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profesional_id", nullable = true)
	private Profesional profesional;
	
	@Column(name = "duracion", nullable = true)
	@Convert(converter = DurationConverter.class)
	private Duration duracion;
	
	@Column(name = "ocupado", nullable = true)
	private boolean ocupado;

}
