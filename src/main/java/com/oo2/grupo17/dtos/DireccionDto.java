package com.oo2.grupo17.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DireccionDto {
	
	private Long id;
	private String calle;
	private int altura;
	private Long localidadId;
	private Long provinciaId;
	
}
