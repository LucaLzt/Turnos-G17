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

import lombok.Builder;

@Controller @Builder
@RequestMapping("/especialidades")
public class EspecialidadController {
	
	private final IEspecialidadService especialidadService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/agregar")
	public String agregarEspecialidad(Model model) {
		model.addAttribute("especialidad", new EspecialidadDto());
		return "/especialidades/agregar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/agregar")
	public String agregarEspecialidadPost(@ModelAttribute("especialidad") EspecialidadDto especialidad) {
		especialidadService.save(especialidad);
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/modificar")
	public String modificarEspecialidad(Model model) {
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("especialidades", especialidades);
		return "/especialidades/lista-modificar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/modificar")
	public String modificarEspecialidad(@PathVariable("id") Long id, Model model) {
		EspecialidadDto especialidad = especialidadService.findById(id);
		model.addAttribute("especialidad", especialidad);
		return "/especialidades/modificar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/modificar")
	public String modificarEspecialidadPost(@PathVariable("id") Long id, @ModelAttribute("especialidad") EspecialidadDto especialidad, 
											BindingResult result) {
		if(result.hasErrors()) {
			return "servicio/modificar";
		}
		especialidadService.update(id, especialidad);
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarEspecialidad(Model model) {
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("especialidades", especialidades);
		return "/especialidades/lista-eliminar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/eliminar")
	public String eliminarEspecialidad(@PathVariable("id") Long id, @ModelAttribute("especialidad") EspecialidadDto especialidad, 
										BindingResult result) {
		if(result.hasErrors()) {
			return "/especialidades/lista-eliminar";
		}
		especialidadService.deleteById(id);
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
}
