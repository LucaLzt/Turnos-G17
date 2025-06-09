package com.oo2.grupo17.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalRegistradoDto {
	
	@NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
	private String email;
	
	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
	private String nombre;
	
	@NotNull(message = "El DNI es obligatorio")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "El DNI no puede superar 8 dígitos")
	private int dni;
	
	@NotNull(message = "La matrícula es obligatoria")
    @Min(value = 1, message = "La matrícula debe ser mayor a 0")
	private Integer matricula;
	
	@NotNull(message = "El móvil es obligatorio")
    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
	private Integer movil;
	
	@Min(value = 10000000, message = "El teléfono debe tener al menos 8 dígitos")
    @Max(value = 99999999, message = "El teléfono no debe superar los 8 dígitos")
	private Integer telefono;
	
}
