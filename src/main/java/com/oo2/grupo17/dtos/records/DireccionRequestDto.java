package com.oo2.grupo17.dtos.records;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DireccionRequestDto (
		
		@NotBlank(message = "La calle es obligatoria")
		String calle,

	    @NotNull(message = "La altura es obligatoria")
		int altura,

	    @NotNull(message = "La provincia es obligatoria")
		Long provinciaId,

	    @NotNull(message = "La localidad es obligatoria")
		Long localidadId
		
){}
