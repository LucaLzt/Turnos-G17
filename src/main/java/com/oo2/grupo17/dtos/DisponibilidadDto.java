package com.oo2.grupo17.dtos;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DisponibilidadDto {
	
	private Long id;
	private ProfesionalDto profesional;
	private LocalDateTime inicio;
	private Duration duracion;
	private boolean ocupado;
	
}
