package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadRequestDto (
		
		@NotBlank String nombre
		
){}
