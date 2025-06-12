package com.oo2.grupo17.dtos.records;

public record RegisterDto (
		String email, 
		String password,
		String nombre,
		int dni,
		int movil,
		int telefono
) {}
