package com.oo2.grupo17.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="Persona")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_persona", discriminatorType=DiscriminatorType.STRING)
public abstract class Persona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@Column(name="dni", nullable = false)
	private int dni;
	
	@Embedded
	private Contacto contacto;
	
	@Column(name="createdat", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name="updatedat")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
