package com.oo2.grupo17.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LocalidadDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private String nombre;
	private Long provinciaId;
	
}
