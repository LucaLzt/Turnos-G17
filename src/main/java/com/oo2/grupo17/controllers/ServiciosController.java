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

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IServicioService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/servicios")
public class ServiciosController {
	
	private final IServicioService servicioService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/agregar")
	public String agregarServicio(Model model) {
		model.addAttribute("servicio", new ServicioDto());
		return ViewRouteHelper.SERVICIOS_AGREGAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/agregar")
	public String agregarServicioPost(@ModelAttribute("servicio") ServicioDto servicio) {
		servicioService.save(servicio);
		return "redirect:admin/administrar-servicios?agregado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/modificar")
	public String modificarServicio(Model model) {
		// Traigo todos los servicios disponibles
		model.addAttribute("servicios", servicioService.findAll());
		return ViewRouteHelper.SERVICIOS_LISTA_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/modificar")
	public String modificarServicio(@ModelAttribute("id") Long id, Model model) {
		ServicioDto servicio = servicioService.findById(id);
		model.addAttribute("servicio", servicio);
		return ViewRouteHelper.SERVICIOS_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/modificar")
	public String modificarServicioPost(@PathVariable("id") Long id, @ModelAttribute("servicio") ServicioDto servicio,BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.SERVICIOS_MODIFICAR;
		}
		servicioService.update(id, servicio);
		return "redirect:/servicios/modificar?modificado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarServicio(Model model) {
		List<ServicioDto> servicios = servicioService.findAll();
		model.addAttribute("servicios", servicios);
		return ViewRouteHelper.SERVICIOS_LISTA_ELIMINAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/eliminar")
	public String eliminarServicio(@ModelAttribute("id") Long id, Model model) {
		servicioService.deleteById(id);
		return "redirect:/servicios/eliminar?eliminado=ok";
	}
}
