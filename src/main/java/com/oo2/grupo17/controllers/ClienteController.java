package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
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
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.IProvinciaService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
	private final IClienteService clienteService;
	private final IContactoService contactoService;
	private final IDireccionService direccionService;
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;

	public ClienteController(IClienteService clienteService, IContactoService contactoService,
			IDireccionService direccionService, IProvinciaService provinciaService,
			ILocalidadService localidadService) {
		this.clienteService = clienteService;
		this.contactoService = contactoService;
		this.direccionService = direccionService;
		this.provinciaService = provinciaService;
		this.localidadService = localidadService;
	}

	// Muestra el perfil del cliente
	@GetMapping("/perfil")
	public String perfil(Model model, Principal principal) {
		
		// Obtiene el email del usuario autenticado y busco al cliente
		String email = principal.getName();
		ClienteDto cliente = clienteService.findByEmail(email);
		
		// Si el cliente existe, lo agrega al modelo
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
		
		return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	// Muestra el formulario para modificar el contacto del cliente
	@GetMapping("/modificar-contacto")
	public String modificarContacto(Model model, Principal principal) {
		String email = principal.getName();
		ContactoDto contacto = contactoService.findByEmail(email);
		if(contacto != null) {
			model.addAttribute("contacto", contacto);
		}
		return ViewRouteHelper.CLIENTE_CONTACTO;
	}
	
	// Actualiza el contacto del cliente
	@PostMapping("/modificar-contacto")
	public String modificarContactoUpdatear (@ModelAttribute("contacto") ContactoDto contactoDto,
			Model model, BindingResult result, Principal principal) {
		
		// Validar los datos del contacto
		if(result.hasErrors()) {
	        return ViewRouteHelper.CLIENTE_CONTACTO;
	    }
		
		// 1. Obtengo el email actual antes de actualizar
		String emailActual = principal.getName();
		
		// 2. Guardo los cambios del contacto (incluyendo la opcion de cambio de mail)
		clienteService.updatearContactoUserEntity(contactoDto);
		
		// 3. Comparo el email nuevo con el viejo
		String emailNuevo = contactoDto.getEmail();
		if(!emailActual.equals(emailNuevo)) {
			SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad si el email cambia
			return "redirect:/auth/login?logout";
		}
		
		// Si no cambió el email, cargo la información del cliente para la vista normal
		ClienteDto cliente = clienteService.findByEmail(emailActual);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
	    return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	// Muestra el formulario para agregar una dirección
	@GetMapping("/agregar-direccion")
	public String agregarDireccion(Model model, Principal principal) {
		
		// Obtengo las listas de provincias y localidades
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		
		// Agrego un objeto DireccionDto y las listas al modelo
		model.addAttribute("direccion", new DireccionDto());
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.CLIENTE_DIRECCION;
	}
	
	// Agrega una nueva dirección y la asocia al contacto del cliente
	@PostMapping("/agregar-direccion")
	public String agregarDireccionUpdatear(@ModelAttribute("direccion") DireccionDto direccionDto,
			Model model, BindingResult result, Principal principal) {
		
		// Validar los datos de la dirección
		if (result.hasErrors()) {
            return ViewRouteHelper.CLIENTE_DIRECCION;
        }
		
		// 1. Verificar que el contacto existe
		String email = principal.getName();
		
		// 2. Si existe, crear y asociar la dirección al contacto
		ContactoDto contacto = contactoService.findByEmail(email);
		
		// 3. Actualizar el contacto con la nueva dirección
		direccionService.crearDireccion(contacto, direccionDto);
		
		// 4. Redirigir al perfil del cliente
		ClienteDto cliente = clienteService.findByEmail(email);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
		return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	// Muestra el formulario para modificar la dirección del cliente
	@GetMapping("/modificar-direccion")
	public String modificarDireccion(Model model, Principal principal) {
		
		// Obtiene el email del usuario autenticado
		String email = principal.getName();
		
		// Obtiene la dirección y la listas de provincias y localidades
		DireccionDto direccion = direccionService.findByContactoEmail(email);
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		
		// Agrega la dirección y las listas al modelo
		model.addAttribute("direccion", direccion);
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.CLIENTE_MOD_DIRECCION;
	}
	
	// Actualiza la dirección del cliente
	@PostMapping("/modificar-direccion")
	public String modificarDireccionUpdatear(@ModelAttribute("direccion") DireccionDto direccionDto,
			Model model, BindingResult result, Principal principal) {
		
		// Validar los datos de la dirección
		if(result.hasErrors()) {
	        return ViewRouteHelper.CLIENTE_MOD_DIRECCION;
	    }
		
		String email = principal.getName();
		ContactoDto contacto = contactoService.findByEmail(email);
		
		direccionService.actualizarDireccion(contacto, direccionDto);
		
		ClienteDto cliente = clienteService.findByEmail(email);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
		return ViewRouteHelper.CLIENTE_PERFIL;
	}
	
	@GetMapping("/home")
	public String index() {
		return ViewRouteHelper.HOME_INDEX;
	}
	
	@GetMapping("/logout")
	public String logout() {
		SecurityContextHolder.clearContext();
		return "redirect:/auth/login?logout";
	}
		
}
