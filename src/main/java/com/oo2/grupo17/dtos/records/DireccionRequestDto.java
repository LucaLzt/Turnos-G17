package com.oo2.grupo17.dtos.records;

import io.swagger.v3.oas.annotations.media.Schema;

public record DireccionRequestDto (
		
		@Schema(example = "Calle Falsa")
		String calle,
		
		@Schema(example = "123")
		int altura,
		
		@Schema(example = "1")
		Long provinciaId,
		
		@Schema(example = "1")
		Long localidadId
		){

}
