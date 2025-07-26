package com.oo2.grupo17.dtos.records;

public record DireccionResponseDto (
		
		Long id,
		String calle,
		int altura,
		Long provinciaId,
		Long localidadId
		
){}
