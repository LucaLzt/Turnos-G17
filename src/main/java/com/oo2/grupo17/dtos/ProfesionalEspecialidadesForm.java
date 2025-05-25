package com.oo2.grupo17.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalEspecialidadesForm {
    private Long profesionalId; // Para saber a qué profesional asignar
    private List<Long> especialidadesSeleccionadas; // IDs de especialidades
}
