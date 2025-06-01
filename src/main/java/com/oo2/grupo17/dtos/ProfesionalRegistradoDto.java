package com.oo2.grupo17.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalRegistradoDto {
	
	private String email;
	private String nombre;
	private Integer dni;
	private Integer matricula;
	private Integer movil;
	private Integer telefono;
	
}
