package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EspecialidadRequestDto (
		
		@NotBlank  String nombre

		
){}
