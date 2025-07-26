package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EspecialidadRequestDto (
		
		@NotBlank(message = "El nombre es obligatorio")
	    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
		String nombre
		
){}
