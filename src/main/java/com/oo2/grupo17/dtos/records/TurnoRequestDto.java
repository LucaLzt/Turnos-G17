package com.oo2.grupo17.dtos.records;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;


public record TurnoRequestDto (
		@Schema(example = "Juan Perez")
		String cliente,
		
		@Schema(example = "Dr. Carlos Lopez")
		String profesional,
		
		@Schema(example = "Consulta médica")
		String servicio,
		
		DireccionRequestDto lugar,
		
		@Schema(example = "2025-10-01T10:00:00")
		LocalDateTime disponibilidad
		){

}
