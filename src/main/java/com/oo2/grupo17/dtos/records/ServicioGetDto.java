package com.oo2.grupo17.dtos.records;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ServicioGetDto (
		Long id,
		String nombre,
		String descripcion,
		Double precio,
		@Schema(example = "[1, 2]")
		List<Long> idsLugares
		)
{}
