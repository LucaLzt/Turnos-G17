package com.oo2.grupo17.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.services.IServicioService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/servicios")
public class ServiciosController {
	
	private final IServicioService servicioService;
	
	@GetMapping("/agregar")
	public String agregarServicio(Model model) {
		model.addAttribute("servicio", new ServicioDto());
		return "servicios/agregar";
	}
	
	@PostMapping("/agregar")
	public String agregarServicioPost(@ModelAttribute("servicio") ServicioDto servicio) {
		servicioService.save(servicio);
		return "admin/administrar-servicios";
	}
	
	@GetMapping("/modificar")
	public String modificarServicio(Model model) {
		// Traigo todos los servicios disponibles
		model.addAttribute("servicios", servicioService.findAll());
		return "servicios/lista-modificar";
	}
	
	@GetMapping("/{id}/modificar")
	public String modificarServicio(@ModelAttribute("id") Long id, Model model) {
		ServicioDto servicio = servicioService.findById(id);
		model.addAttribute("servicio", servicio);
		return "servicios/modificar";
	}
	
	@PostMapping("/{id}/modificar")
	public String modificarServicioPost(@PathVariable("id") Long id, @ModelAttribute("servicio") ServicioDto servicio,BindingResult result) {
		if(result.hasErrors()) {
			return "servicios/agregar";
		}
		servicioService.update(id, servicio);
		return "admin/administrar-servicios";
	}
	
	@GetMapping("/eliminar")
	public String eliminarServicio(Model model) {
		List<ServicioDto> servicios = servicioService.findAll();
		model.addAttribute("servicios", servicios);
		return "servicios/lista-eliminar";
	}
	
	@GetMapping("/{id}/eliminar")
	public String eliminarServicio(@ModelAttribute("id") Long id, Model model) {
		servicioService.deleteById(id);
		return "admin/administrar-servicios";
	}
}
