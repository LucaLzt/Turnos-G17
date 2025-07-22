package com.oo2.grupo17.dtos.records;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterProfesionalDto (
		
		@Schema(description = "Email del profesional. Se enviará la contraseña a este correo.")
		@NotBlank @Email String email, 
		@NotBlank String nombre,
		@NotNull @Min(10000000) @Max(99999999) int dni,
		@NotNull @Min(1) int matricula,
		@NotNull @Min(1000000000) @Max(9999999999L) long movil,
		@Min(1000000) @Max(99999999) int telefono
		
) {}
