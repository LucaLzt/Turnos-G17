package com.oo2.grupo17.dtos;

import java.time.LocalTime;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LugarDto {
	
	private Long id;

    @NotNull(message = "La dirección es obligatoria")
    private DireccionDto direccion;

    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime horarioApertura;

    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime horarioCierre;

    @AssertTrue(message = "El horario de cierre debe ser posterior al de apertura")
    public boolean isHorarioValido() {
        if (horarioApertura == null || horarioCierre == null) return true; // Evita doble error
        return horarioCierre.isAfter(horarioApertura);
    }

	@Override
	public String toString() {
		return "LugarDto [id=" + id + ", direccion=" + direccion + ", horarioApertura=" + horarioApertura
				+ ", horarioCierre=" + horarioCierre + "]";
	}
	
}
