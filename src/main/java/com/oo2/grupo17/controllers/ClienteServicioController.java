package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IServicioService;


@Controller
@RequestMapping("/cliente")
public class ClienteServicioController {
	
	

    @Autowired
    private IServicioService servicioService;
    
    @Autowired
    private ILugarService lugarService;
    
	@GetMapping("/servicios")
	public String servicios(Model model) {
		List<ServicioDto> servicios = servicioService.findAllByOrderByNombreAsc();
		model.addAttribute("servicios", servicios);
		return "cliente/servicios";
	}
	
	@GetMapping("/servicios/{id}/lugares")
	public String lugares(@PathVariable("id") Long servicioId, Model model) {
	
		ServicioDto servicio = servicioService.findById(servicioId);
		Set<LugarDto> lugares = lugarService.findByServiciosId(servicioId);
		model.addAttribute(servicio);
		model.addAttribute(lugares);
		return "cliente/lugar-por-servicio";
	}
	
}
