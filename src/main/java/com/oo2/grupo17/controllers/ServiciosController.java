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

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/servicios")
public class ServiciosController {
	
	private final IServicioService servicioService;
	private final ITurnoService turnoService;
	private final ILugarService lugarService;
	
	@GetMapping("/agregar")
	public String agregarServicio(Model model) {
		model.addAttribute("servicio", new ServicioDto());
		return ViewRouteHelper.SERVICIOS_AGREGAR;
	}
	
	@PostMapping("/agregar")
	public String agregarServicioPost(@Valid @ModelAttribute("servicio") ServicioDto servicio,
			BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.SERVICIOS_AGREGAR;
		}
		servicioService.save(servicio);
		return "redirect:/admin/administrar-servicios?agregado=ok";
	}
	
	@GetMapping("/modificar")
	public String modificarServicio(Model model) {
		// Traigo todos los servicios disponibles
		model.addAttribute("servicios", servicioService.findAll());
		return ViewRouteHelper.SERVICIOS_LISTA_MODIFICAR;
	}
	
	@GetMapping("/{id}/modificar")
	public String modificarServicio(@PathVariable("id") Long id, Model model) {
		ServicioDto servicio = servicioService.findById(id);
		List<LugarDto> lugares = lugarService.findAll();
		model.addAttribute("servicio", servicio);
		model.addAttribute("lugares", lugares);
		return ViewRouteHelper.SERVICIOS_MODIFICAR;
	}
	
	@PostMapping("/{id}/modificar")
	public String modificarServicioPost(@PathVariable("id") Long id, 
			@Valid @ModelAttribute("servicio") ServicioDto servicio, BindingResult result,
			Model model) {
		if(result.hasErrors()) {
			List<LugarDto> lugares = lugarService.findAll();
			model.addAttribute("lugares", lugares);
			return ViewRouteHelper.SERVICIOS_MODIFICAR;
		}
		servicioService.update(id, servicio);
		return "redirect:/servicios/modificar?modificado=ok";
	}
	
	@GetMapping("/eliminar")
	public String eliminarServicio(Model model) {
		List<ServicioDto> servicios = servicioService.findAll();
		model.addAttribute("servicios", servicios);
		
		
		return ViewRouteHelper.SERVICIOS_LISTA_ELIMINAR;
	}
	
	@PostMapping("/{id}/eliminar")
	public String eliminarServicio(@ModelAttribute("id") Long id, Model model) {
		boolean eliminar = turnoService.existsByServicio(id);
		servicioService.deleteById(id, eliminar);
		return "redirect:/servicios/eliminar?eliminado=ok";
	}
}
