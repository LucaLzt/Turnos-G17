package com.oo2.grupo17.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class EspecialidadDto {
	
	private @Setter(AccessLevel.PROTECTED) Long id;
	private String nombre;
	
	/* Opcional: Lista de profesionales si se necesita relación inversa
	private Set<Long> profesionalesIds;
	*/
	
	public EspecialidadDto(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
}
