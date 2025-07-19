package com.oo2.grupo17.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDto {

	private Long id;
	
	@NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 200, message = "La descripción debe tener entre 10 y 200 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 7, fraction = 2, message = "El precio solo puede tener hasta 2 decimales")
    private Double precio;
    
	private List<Long> lugaresIds = new ArrayList<>();

	public ServicioDto(String nombre,String descripcion,Double precio,List<Long> lugaresIds) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.lugaresIds = lugaresIds;
	}
	
	
}
