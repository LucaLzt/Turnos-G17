package com.oo2.grupo17.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDto {

	private Long id;
	private String nombre;
	private String descripcion;
	private Double precio;
	private List<Long> lugaresIds = new ArrayList<>();
	
}
