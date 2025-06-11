package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IDisponibilidadService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IProvinciaService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
@RequestMapping("/profesional")
public class ProfesionalController {
	
	private final IProfesionalService profesionalService;
	private final IDisponibilidadService disponibilidadService;
	private final IContactoService contactoService;
	private final IServicioService servicioService;
	private final IDireccionService direccionService;
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;
	private final ITurnoService turnoService;
	
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
	public String modificarContactoPost (@Valid @ModelAttribute("contacto") ContactoDto contactoDto,
			BindingResult result, Model model, Principal principal) {
		// Validar los datos del contacto
		if(result.hasErrors()) {
			return ViewRouteHelper.PROFESIONAL_CONTACTO;
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
	public String modificarDireccionPost(@Valid @ModelAttribute("direccion") DireccionDto direccionDto,
			BindingResult result, Model model, Principal principal) {
		// Validar los datos de la dirección
		if (result.hasErrors()) {
	        return ViewRouteHelper.PROFESIONAL_DIRECCION;
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
	
	@GetMapping("/servicios-habilitados")
	public String verServiciosHabilitados(Model model, Principal principal) {
		ProfesionalDto profesional = profesionalService.findByEmail(principal.getName());
		List<Long> serviciosIds = profesional.getServiciosIds();
	    List<ServicioDto> servicios = serviciosIds.stream()
	        .map(servicioService::findById)
	        .collect(Collectors.toList());
		model.addAttribute("servicios", servicios);
		return ViewRouteHelper.PROFESIONAL_SERVICIOS;
	}
	
	@GetMapping("/ver-disponibilidades")
	public String verDisponibilidades(Model model, Principal principal) {
	    ProfesionalDto profesional = profesionalService.findByEmail(principal.getName());
	    List<DisponibilidadDto> disponibilidades = disponibilidadService.obtenerDisponibilidadesPorProfesional(profesional.getId());
	    model.addAttribute("profesional", profesional);
	    model.addAttribute("disponibilidades", disponibilidades);
	    return ViewRouteHelper.PROFESIONAL_DISPONIBILIDAD;
	}
	
	@GetMapping("/cancelar-turno")
    public String cancelarTurnosProfesional(Model model, Principal principal) {
    	String email = principal.getName();
    	ProfesionalDto profesional = profesionalService.findByEmail(email);
        Long profesionalId = profesional.getId();
        
        List<Turno> turnos = turnoService.buscarTurnosPorProfesionalId(profesionalId);
        System.out.println(turnos.get(0).getId());
        model.addAttribute("turnos", turnos);

        return ViewRouteHelper.PROFESIONAL_TURNOS_CANCELAR;
    }
	
	@GetMapping("/detalle/{id}")
    public String detalleTurno(@PathVariable("id") long id, Model model) {
        TurnoDto turno = turnoService.findById(id);
        model.addAttribute("turno", turno);
        return ViewRouteHelper.PROFESIONAL_DETALLE_TURNO;
    }
	
	@GetMapping("/eliminar/{id}")
    public String eliminarTurno(@PathVariable Long id) {
        turnoService.deleteById(id);
        return "redirect:/profesional/cancelar-turno"; 
    }
	
	@GetMapping("/disponibilidad/{id}")
	public String verDetallesTurno(@PathVariable Long id, Model model) {
		TurnoDto turno = turnoService.findByIdDisponibilidad(id);
		model.addAttribute("turno", turno);
		return ViewRouteHelper.PROFESIONAL_DETALLE_TURNO;
	}
	
	@GetMapping("/cambiar-contraseña")
	public String cambiarContrasena(Model model) {
		model.addAttribute("cambioPasswordDto", new CambioPasswordDto());
		return ViewRouteHelper.PROFESIONAL_CONTRASEÑA;
	}
	
	@PostMapping("/cambiar-contraseña")
	public String cambiarContrasenaPost(@Valid @ModelAttribute("cambioPasswordDto") CambioPasswordDto cambioPasswordDto,
			BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
			return ViewRouteHelper.PROFESIONAL_CONTRASEÑA;
		}
		ProfesionalDto profesional = profesionalService.findByEmail(principal.getName());
		profesionalService.cambiarContrasena(profesional, cambioPasswordDto);
		return "redirect:/profesional/perfil?cambioContrasena=ok";
	}
	
}
