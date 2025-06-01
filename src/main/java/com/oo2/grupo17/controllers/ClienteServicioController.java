package com.oo2.grupo17.controllers;

import java.util.List;
import com.oo2.grupo17.services.implementation.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IServicioService;


@Controller
@RequestMapping("/cliente")
public class ClienteServicioController {

    private final ContactoService contactoService;
	
	

    @Autowired
    private IServicioService servicioService;
    
    @Autowired
    private ILugarService lugarService;

    ClienteServicioController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }
    
	@GetMapping("/servicios")
	public String servicios(Model model) {
		List<ServicioDto> servicios = servicioService.findAllByOrderByNombreAsc();
		model.addAttribute("servicios", servicios);
		return "cliente/servicios";
	}
	
	@GetMapping("/servicios/{id}/lugares")
	public String lugares(@PathVariable("id") Long servicioId, Model model) {
		ServicioDto servicio = servicioService.findById(servicioId);
		List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(servicioId);
		model.addAttribute("servicio", servicio);
		model.addAttribute("lugares", lugares);	    
		return "cliente/lugar-por-servicio";
	}
	
}
