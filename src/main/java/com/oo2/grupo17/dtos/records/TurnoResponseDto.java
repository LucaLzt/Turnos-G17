package com.oo2.grupo17.dtos.records;

import java.time.LocalDateTime;

public record TurnoResponseDto (
		
        Long id,
        String cliente,
        String profesional,
        String lugar,
        String servicio,
        LocalDateTime disponibilidad
        
){}
