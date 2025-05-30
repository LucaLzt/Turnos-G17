package com.oo2.grupo17.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRegistroDto {

	private String email;
	private String password;
	private String nombre;
	private Integer dni;
	private Integer movil;
	private Integer telefono;
	
}
