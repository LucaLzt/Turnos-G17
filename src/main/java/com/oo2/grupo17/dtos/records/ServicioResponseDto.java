package com.oo2.grupo17.dtos.records;

import java.util.List;

public record ServicioResponseDto (

		Long id,
		String nombre,
		String descripcion,
		double precio,
		List<Long> lugaresIds
		
){}
