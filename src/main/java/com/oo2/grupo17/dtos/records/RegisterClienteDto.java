package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterClienteDto (
		
	    @NotBlank(message = "El email es obligatorio")
	    @Email(message = "El email no es válido")
		String email, 
		
	    @NotBlank(message = "La contraseña es obligatoria")
	    @Size(min = 6, max = 32, message = "La contraseña debe tener entre 6 y 32 caracteres")
		String password,

	    @NotBlank(message = "El nombre es obligatorio")
	    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
		String nombre,
		
	    @NotNull(message = "El dni es obligatorio")
	    @Min(value = 10000000, message = "El dni debe tener al menos 8 dígitos")
	    @Max(value = 99999999, message = "El dni no debe superar los 8 dígitos")
		int dni,

	    @NotNull(message = "El móvil es obligatorio")
	    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
	    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
		long movil,

	    @Min(value = 1000000, message = "El teléfono debe tener al menos 7 dígitos")
	    @Max(value = 9999999999L, message = "El teléfono no debe superar los 10 dígitos")
		int telefono
		
) {}
