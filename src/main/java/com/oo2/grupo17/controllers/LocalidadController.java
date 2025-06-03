package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oo2.grupo17.dtos.LocalidadDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.services.ILocalidadService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/localidades")
public class LocalidadController {
	
	private final ILocalidadService localidadService;
	
	@GetMapping("por-provincia/{idProvincia}")
	@ResponseBody
	public List<LocalidadDto> getLocalidadesPorProvincia(@PathVariable Long idProvincia) {
		List<Localidad> localidades = localidadService.getLocalidadesPorProvincia(idProvincia);
		return localidades.stream()
				.map(loc -> new LocalidadDto(loc.getId(), loc.getNombre()))
				.collect(Collectors.toList());
	}
	
}
