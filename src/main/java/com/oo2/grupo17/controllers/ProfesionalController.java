package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;

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

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IEspecialidadService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IProvinciaService;
import com.oo2.grupo17.services.IServicioService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/profesionales")
public class ProfesionalController {
	
	public final IProfesionalService profesionalService;
	public final IContactoService contactoService;
	public final IServicioService servicioService;
	public final ILugarService lugarService;
	public final IEspecialidadService especialidadService;
	private final IDireccionService direccionService;
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		return ViewRouteHelper.PROFESIONAL_LISTA_ELIMINAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/eliminar")
	public String eliminarProfesional(@ModelAttribute("id") Long id, Model model) {
		profesionalService.deleteById(id);
		return "redirect:/profesionales/eliminar?eliminado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/modificar")
	public String modificarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);	
		return ViewRouteHelper.PROFESIONAL_LISTA_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/modificar")
	public String modificarProfesional(@PathVariable("id") Long id, Model model) {
		ProfesionalDto profesional = profesionalService.findById(id);
		model.addAttribute("profesional", profesional);
		return ViewRouteHelper.PROFESIONAL_MODIFICAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/modificar")
	public String modificarProfesionalPost(@PathVariable("id") Long id, 
			@ModelAttribute("profesional") 
			ProfesionalDto profesional, BindingResult result) {
		if(result.hasErrors()) {
			return 	ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
		}
		profesionalService.update(id, profesional);
		return "redirect:/profesionales/modificar?modificado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/gestion")
	public String gestionarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		List<ServicioDto> servicios = servicioService.findAll();
		List<LugarDto> lugares = lugarService.findAll();
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("servicios", servicios);
		model.addAttribute("lugares", lugares);
		model.addAttribute("especialidades", especialidades);		
		return ViewRouteHelper.PROFESIONAL_LISTA_GESTION;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/gestion")
	public String gestionarProfesional(@PathVariable("id") Long id, Model model) {
		ProfesionalDto profesional = profesionalService.findById(id);
		List<ServicioDto> servicios = servicioService.findAll();
		List<LugarDto> lugares = lugarService.findAll();
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("profesional", profesional);
		model.addAttribute("servicios", servicios);
		model.addAttribute("lugares", lugares);
		model.addAttribute("especialidades", especialidades);
		return ViewRouteHelper.PROFESIONAL_GESTION;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/gestion")
	public String gestionarProfesionalPost(@PathVariable("id") Long id, 
			@RequestParam(value = "especialidadId") Long especialidadId,
			@RequestParam(value = "serviciosId") Set<Long> serviciosId,
			@RequestParam(value = "lugarId") Long lugarId) {
		profesionalService.asignarDatosProfesional(id, especialidadId, lugarId, serviciosId);
		return "redirect:/profesionales/gestion?gestionado=ok";
	};
	
	
	@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
	@GetMapping("/perfil")
	public String perfil(Model model, Principal principal) {
		// Obtiene el email del usuario autenticado y busco al profesional
		String email = principal.getName();
		ProfesionalDto profesional = profesionalService.findByEmail(email);
		// Si el cliente existe, lo agrega al modelo
		if(profesional != null) {
			model.addAttribute("profesional", profesional);
		}
		return ViewRouteHelper.PROFESIONAL_PERFIL;
	}
	
	// Muestra el formulario para modificar el contacto del profesional
	@GetMapping("/modificar-contacto")
	public String modificarContacto(Model model, Principal principal) {
		String email = principal.getName();
		ContactoDto contacto = contactoService.findByEmail(email);
		if(contacto != null) {
			model.addAttribute("contacto", contacto);
		}
		return ViewRouteHelper.PROFESIONAL_CONTACTO;
	}
		
	// Actualiza el contacto del profesional
	@PostMapping("/modificar-contacto")
	public String modificarContactoPost (@ModelAttribute("contacto") ContactoDto contactoDto,
			Model model, BindingResult result, Principal principal) {
			
		// Validar los datos del contacto
		if(result.hasErrors()) {
			return ViewRouteHelper.CLIENTE_CONTACTO;
		}
			
		// 1. Obtengo el email actual antes de actualizar
		String emailActual = principal.getName();
			
		// 2. Guardo los cambios del contacto (incluyendo la opcion de cambio de mail)
		profesionalService.updatearContactoUserEntity(contactoDto);
			
		// 3. Comparo el email nuevo con el viejo
		String emailNuevo = contactoDto.getEmail();
		if(!emailActual.equals(emailNuevo)) {
			SecurityContextHolder.clearContext(); // Limpiar el contexto de seguridad si el email cambia
			return "redirect:/auth/login?logout";
		}
			
		return "redirect:/profesionales/perfil?updateContacto=ok";
	}
	
	// Muestra el formulario para agregar una dirección
	@GetMapping("/modificar-direccion")
	public String modificarDireccion(Model model, Principal principal) {
			
		// 1. Obtengo las listas de provincias y localidades
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
			
		// 2. Verifico si el contacto tiene una dirección asocidada
		String email = principal.getName();
		ContactoDto contacto = contactoService.findByEmail(email);
		if(contacto.getDireccion() == null) {
			model.addAttribute("direccion", new DireccionDto()); // Se pasa un objeto vacío
		} else {
			DireccionDto direccion = direccionService.findByContactoEmail(email); // Se pasa un objeto existent
			model.addAttribute("direccion", direccion);
		}
			
		// 3. Agrego las listas al modelo
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.PROFESIONAL_DIRECCION;
	}
		
	// Agrega una nueva dirección y la asocia al contacto del profesional
	@PostMapping("/modificar-direccion")
	public String modificarDireccionPost(@ModelAttribute("direccion") DireccionDto direccionDto,
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
		if(contacto.getDireccion() == null) {
			direccionService.crearDireccion(contacto, direccionDto);
		} else {
			direccionService.actualizarDireccion(contacto, direccionDto);
		}
			
		// 4. Redirigir al perfil del profesional
		ProfesionalDto profesional = profesionalService.findByEmail(email);
		if(profesional != null) {
			model.addAttribute("profesional", profesional);
		}
		return "redirect:/profesionales/perfil?updateDireccion=ok";
	}
	
}
