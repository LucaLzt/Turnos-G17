package com.oo2.grupo17.dtos.records;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record LugarRequestDto (

	    @NotNull(message = "La dirección es obligatoria")
		DireccionRequestDto direccion,

	    @NotNull(message = "El horario de apertura es obligatorio")
		LocalTime horarioApertura,

	    @NotNull(message = "El horario de cierre es obligatorio")
		LocalTime horarioCierre
		
){}
