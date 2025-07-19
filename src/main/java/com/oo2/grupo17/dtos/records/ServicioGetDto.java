package com.oo2.grupo17.dtos.records;

import java.util.List;

public record ServicioGetDto (
		Long id,
		String nombre,
		String descripcion,
		Double precio,
		List<Long> idsLugares
		)
{}
