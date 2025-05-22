package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ServicioDto {
	
	protected @Setter(AccessLevel.PROTECTED) Long id;
	
	private String nombre;
	private String descripcion;
	private int duracion;
	private double precio;
	
	public ServicioDto(String nombre, String descripcion, int duracion, double precio) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.duracion = duracion;
		this.precio = precio;
	}
	
	
	
}
