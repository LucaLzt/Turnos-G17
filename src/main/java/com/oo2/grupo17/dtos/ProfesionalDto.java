package com.oo2.grupo17.dtos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.oo2.grupo17.entities.Contacto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProfesionalDto extends PersonaDto {
	
	@NotNull(message = "La matrícula es obligatoria")
    @Min(value = 1, message = "La matrícula debe ser mayor a 0")
    private Integer matricula;
	
	private EspecialidadDto especialidad;
	private List<Long> serviciosIds = new ArrayList<>();
	private LugarDto lugar;
	private Set<TurnoDto> turnos = new HashSet<>();
	
	public ProfesionalDto(String nombre, int dni, Contacto contacto,Integer matricula,
			EspecialidadDto especialidad, List<Long> serviciosIds, LugarDto lugar, Set<TurnoDto> turnos) {
		super(nombre, dni, contacto);
		this.matricula = matricula;
		this.especialidad = especialidad;
		this.serviciosIds = serviciosIds;
		this.lugar = lugar;
		this.turnos = turnos;
	}

	
	
}
