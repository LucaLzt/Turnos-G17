package com.oo2.grupo17.dtos.records;

import java.time.Duration;
import java.time.LocalDateTime;

public record DisponibilidadResponseDto(
		Long id,
		String profesional,
		LocalDateTime fechaHora,
		Duration duracion,
		boolean ocupado
		) {

}
