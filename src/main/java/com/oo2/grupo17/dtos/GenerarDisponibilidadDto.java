package com.oo2.grupo17.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerarDisponibilidadDto {
	
	@NotNull(message = "Debe seleccionar un profesional")
    private Long profesionalId;

    @NotNull(message = "Debe ingresar la hora de inicio")
    private LocalTime horaInicio;

    @NotNull(message = "Debe ingresar la hora de fin")
    private LocalTime horaFin;

    @NotNull(message = "Debe ingresar la duración")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    @Max(value = 180, message = "La duración máxima recomendada es 180 minutos")
    private Integer duracionMinutos;

    @NotNull(message = "Debe ingresar la fecha de inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "Debe ingresar la fecha de fin")
    private LocalDate fechaFin;
    
    @AssertTrue(message = "La hora de fin debe ser posterior a la hora de inicio")
    public boolean isRangoHorasValido() {
        if (horaInicio == null || horaFin == null) return true; // No mostrar error duplicado
        return horaFin.isAfter(horaInicio);
    }

    @AssertTrue(message = "La fecha de fin debe ser posterior o igual a la fecha de inicio")
    public boolean isRangoFechasValido() {
        if (fechaInicio == null || fechaFin == null) return true;
        return !fechaFin.isBefore(fechaInicio);
    }
    
}
