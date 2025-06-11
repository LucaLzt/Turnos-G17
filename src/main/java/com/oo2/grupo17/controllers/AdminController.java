package com.oo2.grupo17.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oo2.grupo17.dtos.DatosContactoDto;
import com.oo2.grupo17.dtos.GenerarDisponibilidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IEmailService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
	
	private final IProfesionalService profesionalService;
	private final ITurnoService turnoService;
	private final ILugarService lugarService;
	private final IEmailService emailService;

	@GetMapping("/registrar-profesional")
	public String registrarProfesional(Model model) {
		model.addAttribute("profesional", new ProfesionalRegistradoDto());
		return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
	}
	
	@PostMapping("/registrar-profesional")
	public String registrarProfesionalPost(@Valid @ModelAttribute("profesional") ProfesionalRegistradoDto profesionalDto,
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
	
	@GetMapping("/administrar-especialidades")
	public String administrarEspecialidades() {
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
	
	@GetMapping("/administrar-turnos")
	public String administrarTurnos(Model model) {
		List<TurnoDto> turnos = turnoService.findAll();
		model.addAttribute("turnos", turnos);
		return ViewRouteHelper.ADMIN_TURNOS;
	}
	
	@GetMapping("/turnos/{id}/modificar-turnos")
	public String modificarTurnos(@PathVariable("id") Long id, Model model) {
		TurnoDto turno= turnoService.findById(id);
		List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(turno.getServicio().getId());
		model.addAttribute("turno", turno);
		model.addAttribute("lugares", lugares);
		
		return ViewRouteHelper.TURNO_MODIFICAR_TURNO;
	}
	
	@PostMapping("/turnos/{id}/modificar-turnos")
	public String modificarTurnosPost(@PathVariable("id") Long id,@ModelAttribute("turno") TurnoDto turno, @RequestParam("lugarId") Long lugarId) {
		
	  
		LugarDto lugar = lugarService.findById(lugarId);
		turno.setLugar(lugar);
		turnoService.update(id,turno);
		
		return "redirect:/admin/administrar-turnos?turnoModificado=ok";
	}
	
	@GetMapping("/generar-disponibilidades")
	public String generarDisponibilidades(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("datosFormulario", new GenerarDisponibilidadDto());
		return ViewRouteHelper.PROFESIONAL_DISPONIBILIDADES;
	}
	
	@PostMapping("/generar-disponibilidades")
	public String generarDisponibilidadesPost(@Valid @ModelAttribute("datosFormulario") GenerarDisponibilidadDto dto,
			BindingResult result, Model model) {
		if(result.hasErrors()) {
			List<ProfesionalDto> profesionales = profesionalService.findAll();
			model.addAttribute("profesionales", profesionales);
			return ViewRouteHelper.PROFESIONAL_DISPONIBILIDADES;
		}
		profesionalService.generarDisponibilidadesAutomaticas(dto);
		return "redirect:/admin/administrar-profesional?disponibilidadesGeneradas=ok";
	}
	
	@GetMapping("/contactar-profesional")
	public String contactarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("datosContacto", new DatosContactoDto());
		return "/admin/contactar-profesional";
	}
	
	@PostMapping("/contactar-profesional")
	public String contactarProfesionalPost(@Valid @ModelAttribute("datosContacto") DatosContactoDto dto,
			BindingResult result, Model model) {
		if(result.hasErrors()) {
			List<ProfesionalDto> profesionales = profesionalService.findAll();
			model.addAttribute("profesionales", profesionales);
			return "admin/contactar-profesional";
		}
		ProfesionalDto profesional = profesionalService.findById(dto.getProfesionalId());
		emailService.enviarEmail(profesional.getContacto().getEmail(), dto.getAsunto(), dto.getMensaje());
		return "redirect:/index?mensajeEnviado=ok";
	}
	
	@GetMapping("/logout")
	public String logout() {
		SecurityContextHolder.clearContext();
		return "redirect:/auth/login?logout";
	}

}
