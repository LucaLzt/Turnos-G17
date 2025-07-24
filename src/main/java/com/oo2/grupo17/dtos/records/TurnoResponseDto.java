package com.oo2.grupo17.dtos.records;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record TurnoResponseDto (
		@Schema(example = "1")
		Long id,
		
		@Schema(example = "Juan Perez")
		String cliente,
		
		@Schema(example = "Dr. Carlos Lopez")
		String profesional,
		
		@Schema(example = "Calle Falsa 123, ProvinciaId, LocalidadId")
		String lugar,
		
		@Schema(example = "Consulta médica")
		String servicio,
		
		@Schema(example = "2023-10-01T10:00:00")
		LocalDateTime disponibilidad
		){

}
