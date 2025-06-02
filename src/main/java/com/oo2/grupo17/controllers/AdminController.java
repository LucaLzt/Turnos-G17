package com.oo2.grupo17.controllers;

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

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final IProfesionalService profesionalService;
	
	public AdminController(IProfesionalService profesionalService) {
		this.profesionalService = profesionalService;
	}

	@GetMapping("/registrar-profesional")
	public String registrarProfesional(Model model) {
		model.addAttribute("profesional", new ProfesionalRegistradoDto());
		return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
	}
	
	@PostMapping("/registrar-profesional")
	public String registrarProfesionalPost(@ModelAttribute("profesional") ProfesionalRegistradoDto profesionalDto,
			BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
		}
		profesionalService.registrarProfesional(profesionalDto);
		return "redirect:/index?registroExitoso";
	}
	
	@GetMapping("/administrar-servicios")
	public String administrarServicios() {
		return ViewRouteHelper.ADMIN_SERVICIOS;
	}
	
	@GetMapping("/administrar-lugares")
	public String administrarLugares() {
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
	@GetMapping("/administrar-profesional")
	public String administrarProfesional() {
		return ViewRouteHelper.ADMIN_PROFESIONAL;
	}
}
