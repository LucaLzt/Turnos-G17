package com.oo2.grupo17.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRegistroDto {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 32, message = "La contraseña debe tener entre 6 y 32 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 40, message = "El nombre debe tener entre 2 y 40 caracteres")
    private String nombre;

    @NotNull(message = "El móvil es obligatorio")
    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
    private int dni;

    @NotNull(message = "El móvil es obligatorio")
    @Min(value = 1000000000, message = "El móvil debe tener al menos 10 dígitos")
    @Max(value = 9999999999L, message = "El móvil no debe superar los 10 dígitos")
    private int movil;

    @Min(value = 1000000, message = "El teléfono debe tener al menos 7 dígitos")
    @Max(value = 9999999999L, message = "El teléfono no debe superar los 10 dígitos")
    private int telefono;
    
}
