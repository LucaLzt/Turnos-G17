package com.oo2.grupo17.dtos.records;

public record ClienteResponseDto (
		
		Long id,
		String nombre,
		int dni,
		String nroCliente,
		ContactoResponseDto contacto
		
){}
