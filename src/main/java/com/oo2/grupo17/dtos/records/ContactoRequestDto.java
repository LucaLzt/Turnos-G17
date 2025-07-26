package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactoRequestDto (

		@NotBlank(message = "El email es obligatorio")
	    @Email(message = "El email no es válido")
		String email,
		
		@NotNull(message = "El móvil es obligatorio")
	    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
	    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
		long movil,
		
		@Min(value = 10000000, message = "El teléfono debe tener al menos 8 dígitos")
	    @Max(value = 99999999, message = "El teléfono no debe superar los 8 dígitos")
		int telefono,
		
		@NotNull 
		DireccionRequestDto direccion
		
){}
