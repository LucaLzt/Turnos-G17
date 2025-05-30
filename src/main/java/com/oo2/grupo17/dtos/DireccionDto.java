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
	
}
