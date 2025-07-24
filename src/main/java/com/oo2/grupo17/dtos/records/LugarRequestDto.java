package com.oo2.grupo17.dtos.records;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record LugarRequestDto (
		
		@NotNull DireccionRequestDto direccion,
		@NotNull LocalTime horarioApertura,
		@NotNull LocalTime horarioCierre
		
){}
