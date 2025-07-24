package com.oo2.grupo17.dtos.records;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TurnoRequestDto (
		
		@NotBlank String profesional,
		@NotBlank String direccion,
		@NotNull int altura,
		@NotBlank String servicio,
		@NotNull LocalDateTime disponibilidad
		
){}
