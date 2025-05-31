package com.oo2.grupo17.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	private final IClienteService clienteService;
	private final IContactoService contactoService;
	private final IDireccionService direccionService;

	public ClienteController(IClienteService clienteService, IContactoService contactoService
			, IDireccionService direccionService) {
		this.clienteService = clienteService;
		this.contactoService = contactoService;
		this.direccionService = direccionService;
	}

	@GetMapping("/perfil")
	public String perfil(Model model, Principal principal) {
		String email = principal.getName();
		ClienteDto cliente = clienteService.findByEmail(email);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
		return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	@GetMapping("/modificar-contacto")
	public String modificarContacto(Model model, Principal principal) {
		String email = principal.getName();
		ContactoDto contacto = contactoService.findByEmail(email);
		if(contacto != null) {
			model.addAttribute("contacto", contacto);
		}
		return ViewRouteHelper.CLIENTE_CONTACTO;
	}
	
	@PostMapping("/modificar-contacto")
	public String modificarContactoUpdatear (@ModelAttribute("contacto") ContactoDto contactoDto,
			Model model, BindingResult result, Principal principal) {
		if(result.hasErrors()) {
	        return ViewRouteHelper.CLIENTE_CONTACTO;
	    }
		// Guardo los cambios del contacto
		contactoService.update(contactoDto.getId(), contactoDto);
		// Vuelvo a cargar el perfil del cliente, contacto y servicio para la vista
		String email = principal.getName();
		ClienteDto cliente = clienteService.findByEmail(email);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
	    return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	@GetMapping("/modificar-direccion")
	public String modificarDireccion(Model model, Principal principal) {
		String email = principal.getName();
		DireccionDto direccion = direccionService.findByContactoEmail(email);
		if(direccion != null) {
			model.addAttribute("direccion", direccion);
		}
		return ViewRouteHelper.CLIENTE_DIRECCION;
	}
		
}
