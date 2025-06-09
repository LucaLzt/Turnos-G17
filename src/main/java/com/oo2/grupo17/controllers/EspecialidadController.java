package com.oo2.grupo17.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IEspecialidadService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/especialidades")
public class EspecialidadController {
	
	private final IEspecialidadService especialidadService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/agregar")
	public String agregarEspecialidad(Model model) {
		model.addAttribute("especialidad", new EspecialidadDto());
		return ViewRouteHelper.ESPECIALIDADES_AGREGAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/agregar")
	public String agregarEspecialidadPost(@Valid @ModelAttribute("especialidad") EspecialidadDto especialidad,
			BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.ESPECIALIDADES_AGREGAR;
		}
		especialidadService.save(especialidad);
		return "redirect:/admin/administrar-especialidades?agregado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/modificar")
	public String modificarEspecialidad(Model model) {
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("especialidades", especialidades);
		return ViewRouteHelper.ESPECIALIDADES_LISTA_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/modificar")
	public String modificarEspecialidad(@PathVariable("id") Long id, Model model) {
		EspecialidadDto especialidad = especialidadService.findById(id);
		model.addAttribute("especialidad", especialidad);
		return ViewRouteHelper.ESPECIALIDADES_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/modificar")
	public String modificarEspecialidadPost(@PathVariable("id") Long id, 
			@Valid @ModelAttribute("especialidad") EspecialidadDto especialidad, BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.ESPECIALIDADES_MODIFICAR;
		}
		especialidadService.update(id, especialidad);
		return "redirect:/especialidades/modificar?modificado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarEspecialidad(Model model) {
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("especialidades", especialidades);
		return ViewRouteHelper.ESPECIALIDADES_LISTA_ELIMINAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/eliminar")
	public String eliminarEspecialidadPost(@PathVariable("id") Long id) {
		especialidadService.deleteById(id);
		return "redirect:/especialidades/eliminar?eliminado=ok";
	}
	
}
