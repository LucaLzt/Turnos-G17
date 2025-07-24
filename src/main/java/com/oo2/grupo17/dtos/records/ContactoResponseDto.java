package com.oo2.grupo17.dtos.records;

public record ContactoResponseDto (

		Long id,
		String email,
		int movil,
		int telefono,
		DireccionResponseDto direccion
		
){}
