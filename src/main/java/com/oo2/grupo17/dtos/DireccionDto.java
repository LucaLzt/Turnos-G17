package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DireccionDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	
	private String calle;
	private int altura;
	private String localidad;
	private String provincia;
	
	public DireccionDto(String calle, int altura, String localidad, String provincia) {
		super();
		this.calle = calle;
		this.altura = altura;
		this.localidad = localidad;
		this.provincia = provincia;
	}
	
}
