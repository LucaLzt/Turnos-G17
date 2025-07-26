package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfesionalRequestDto (

		@NotBlank(message = "El nombre es obligatorio")
	    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
		String nombre,
		
	    @NotNull(message = "El DNI es obligatorio")
	    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
	    @Max(value = 99999999, message = "El DNI no puede superar 8 dígitos")
		int dni,
		
		@NotNull(message = "La matrícula es obligatoria")
	    @Min(value = 1, message = "La matrícula debe ser mayor a 0")
		Integer matricula 
		
){}
