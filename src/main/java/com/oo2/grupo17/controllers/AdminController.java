package com.oo2.grupo17.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IProfesionalService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/admin")
public class AdminController {
	
	private final IProfesionalService profesionalService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/registrar-profesional")
	public String registrarProfesional(Model model) {
		model.addAttribute("profesional", new ProfesionalRegistradoDto());
		return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/registrar-profesional")
	public String registrarProfesionalPost(@ModelAttribute("profesional") ProfesionalRegistradoDto profesionalDto,
			BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
		}
		profesionalService.registrarProfesional(profesionalDto);
		return "redirect:/index?registroExitoso";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrar-servicios")
	public String administrarServicios() {
		return ViewRouteHelper.ADMIN_SERVICIOS;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrar-lugares")
	public String administrarLugares() {
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrar-profesional")
	public String administrarProfesional() {
		return ViewRouteHelper.ADMIN_PROFESIONAL;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrar-especialidades")
	public String administrarEspecialidades() {
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/logout")
	public String logout() {
		SecurityContextHolder.clearContext();
		return "redirect:/auth/login?logout";
	}

}
