package com.oo2.grupo17.dtos.records;

import java.util.List;

public record ProfesionalResponseAuxDto (

		Long id,
		String nombre,
		int dni,
		ContactoResponseDto contacto,
		Integer matricula,
		EspecialidadResponseDto especialidad,
		List<Long> serviciosIds,
		Long lugarId
		
){}
