package com.oo2.grupo17.dtos.records;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GenerarDisponibilidadesRecordDto (
		
		@NotNull(message = "El ID del profesional es obligatorio")
	    @Positive(message = "El ID del profesional debe ser positivo")
	    Long profesionalId,
		
		@NotNull(message = "La hora de inicio es obligatoria")
	    @JsonFormat(pattern = "HH:mm:ss")
	    LocalTime horaInicio,
	    
	    @NotNull(message = "La hora de fin es obligatoria")
	    @JsonFormat(pattern = "HH:mm:ss")
	    LocalTime horaFin,
	    
	    @NotNull(message = "La duración en minutos es obligatoria")
	    @Min(value = 15, message = "La duración mínima es 15 minutos")
	    @Max(value = 480, message = "La duración máxima es 8 horas (480 minutos)")
	    Integer duracionMinutos,
		
		@NotNull(message = "La fecha de inicio es obligatoria")
	    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o futura")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    LocalDate fechaInicio,
	    
	    @NotNull(message = "La fecha de fin es obligatoria")
	    @Future(message = "La fecha de fin debe ser futura")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    LocalDate fechaFin
		
){}
