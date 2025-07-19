package com.oo2.grupo17.dtos.records;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServicioPutDto (
		@NotNull(message = "El nombre es obligatorio.")
		@Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres.")
		String nombre,
		
		@NotBlank(message = "La descripcion es obligatoria.")
		@Size(min = 10, max = 100, message = "La descripcion debe tener entre 10 y 100 caracteres.")
		String descripcion,
		
		@NotNull(message = "El precio es obligatorio.")
		@DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
	    @Digits(integer = 7, fraction = 2, message = "El precio solo puede tener hasta 2 decimales")
		Double precio,
		
		@Schema(example = "[1, 2]")
		List<Long> idsLugares
		){

}
