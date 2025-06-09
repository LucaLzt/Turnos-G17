package com.oo2.grupo17.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDto {

    private Long id;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotNull(message = "El móvil es obligatorio")
    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
    private int movil;

    @Min(value = 10000000, message = "El teléfono debe tener al menos 8 dígitos")
    @Max(value = 99999999, message = "El teléfono no debe superar los 8 dígitos")
    private int telefono;

    private DireccionDto direccion;
}
