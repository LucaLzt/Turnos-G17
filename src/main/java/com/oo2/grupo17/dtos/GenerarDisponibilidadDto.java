package com.oo2.grupo17.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class GenerarDisponibilidadDto {
	private Long profesionalId;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private Integer duracionMinutos;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
}
