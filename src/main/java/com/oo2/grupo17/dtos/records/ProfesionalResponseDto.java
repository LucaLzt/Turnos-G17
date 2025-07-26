package com.oo2.grupo17.dtos.records;

public record ProfesionalResponseDto (
		Long id,
		String nombre,
		int dni,
		Integer matricula,
		ContactoResponseDto contacto
		){

}
