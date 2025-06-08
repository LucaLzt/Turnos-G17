package com.oo2.grupo17.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DireccionDto {
	
	private Long id;
	
	@NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotNull(message = "La altura es obligatoria")
    private int altura;

    @NotNull(message = "La provincia es obligatoria")
    private Long provinciaId;

    @NotNull(message = "La localidad es obligatoria")
    private Long localidadId;
	
}
