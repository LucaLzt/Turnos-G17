package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DireccionRequestDto (
		
		@NotBlank String calle,
		@NotNull int altura,
		@NotNull Long provinciaId,
		@NotNull Long localidadId
		
){}
