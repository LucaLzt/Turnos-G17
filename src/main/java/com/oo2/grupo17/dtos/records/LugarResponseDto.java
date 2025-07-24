package com.oo2.grupo17.dtos.records;

import java.time.LocalTime;

public record LugarResponseDto (

		Long id,
		DireccionResponseDto direccion,
		LocalTime horarioApertura,
		LocalTime horarioCierre
		
){}
