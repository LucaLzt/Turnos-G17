package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterClienteDto (
		
		@NotBlank @Email String email, 
		@NotBlank String password,
		@NotBlank String nombre,
		@NotNull @Min(10000000) @Max(99999999) int dni,
		@NotNull @Min(1000000000) @Max(9999999999L) int movil,
		@Min(1000000) @Max(99999999) int telefono
		
) {}
