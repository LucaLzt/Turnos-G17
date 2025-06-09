package com.oo2.grupo17.dtos;

import com.oo2.grupo17.entities.Contacto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PersonaDto {
	
	protected Long id;
	
	@NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    private String nombre;

    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
    @Max(value = 99999999, message = "El DNI no puede superar 8 dígitos")
	private int dni;
    
	private Contacto contacto;

	public PersonaDto(String nombre, int dni, Contacto contacto) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.contacto = contacto;
	}
	
}
