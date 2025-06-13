package com.oo2.grupo17.dtos.records;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServicioAgregarDto (
		
		@NotBlank(message = "El nombre es obligatorio")
	    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
		String nombre,
		
		@NotBlank(message = "La descripción es obligatoria")
	    @Size(min = 10, max = 200, message = "La descripción debe tener entre 10 y 200 caracteres")
		String descripcion,
		
		@NotNull(message = "El precio es obligatorio")
	    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
	    @Digits(integer = 7, fraction = 2, message = "El precio solo puede tener hasta 2 decimales")
		Double precio
		){	
}
