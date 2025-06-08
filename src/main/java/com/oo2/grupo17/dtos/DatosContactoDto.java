package com.oo2.grupo17.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatosContactoDto {
	
	@NotNull(message = "Debe seleccionar un profesional.")
    private Long profesionalId;

    @NotBlank(message = "El asunto no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El asunto debe tener entre 3 y 100 caracteres.")
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    @Size(min = 10, max = 1000, message = "El mensaje debe tener entre 10 y 1000 caracteres.")
    private String mensaje;
	
}
