package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicioRequestDto (
		
		@NotBlank String nombre,
		@NotBlank String descripcion,
		@NotNull double precio
		
){}
