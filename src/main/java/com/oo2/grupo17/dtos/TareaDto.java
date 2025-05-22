package com.oo2.grupo17.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TareaDto {
	
	private Long idTarea;
	private String nombre;
	
	/* Opcional: Lista de profesionales si se necesita relación inversa
	private Set<Long> profesionalesIds;
	*/
	
	public TareaDto(Long idTarea, String nombre) {
		super();
		this.idTarea = idTarea;
		this.nombre = nombre;
	}
	
}
