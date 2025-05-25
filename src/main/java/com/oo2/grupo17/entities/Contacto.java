package com.oo2.grupo17.entities;
	
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class Contacto {
	
	private String email;
	private int movil;
	private int telefono;
	
	@Embedded
	private Direccion direccion;
	
}
