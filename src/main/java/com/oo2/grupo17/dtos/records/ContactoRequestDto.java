package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactoRequestDto (

		@NotBlank @Email String email,
		@NotNull int movil,
		@NotNull int telefono,
		@NotNull DireccionRequestDto direccion
		
){}
