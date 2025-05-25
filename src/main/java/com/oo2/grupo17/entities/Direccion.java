package com.oo2.grupo17.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class Direccion {
	
	private String calle;
	private int altura;
	private String localidad;
	private String provincia;
	
}
