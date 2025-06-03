package com.oo2.grupo17.entities;
	
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "Contacto")
public class Contacto {
	
	@Id
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@MapsId
	@JoinColumn(name = "id")
	private Persona persona;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "movil")
	private int movil;
	
	@Column(name = "telefono")
	private int telefono;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "direccion_id")
	private Direccion direccion;
	
}
