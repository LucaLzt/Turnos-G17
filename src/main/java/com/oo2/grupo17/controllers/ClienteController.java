package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IDisponibilidadService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProvinciaService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/cliente")
@PreAuthorize("hasRole('ROLE_CLIENTE')")
public class ClienteController {

    private final IServicioService servicioService;
    private final ILugarService lugarService;
	private final IClienteService clienteService;
	private final IContactoService contactoService;
	private final IDireccionService direccionService;
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;
	private final IDisponibilidadService disponibilidadService;
	private final ITurnoService turnoService;

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
	public String modificarContactoPost (@Valid @ModelAttribute("contacto") ContactoDto contactoDto,
			BindingResult result, Model model,  Principal principal) {
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
		return "redirect:/cliente/perfil?updateContacto=ok";
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
		return ViewRouteHelper.CLIENTE_DIRECCION;
	}
	
	// Agrega una nueva dirección y la asocia al contacto del cliente
	@PostMapping("/modificar-direccion")
	public String modificarDireccionPost(@Valid @ModelAttribute("direccion") DireccionDto direccionDto,
			BindingResult result, Model model ,Principal principal) {
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
		// 4. Redirigir al perfil del cliente
		ClienteDto cliente = clienteService.findByEmail(email);
		if(cliente != null) {
			model.addAttribute("cliente", cliente);
		}
		return "redirect:/cliente/perfil?updateDireccion=ok";
	}
	
	@GetMapping("/servicios")
	public String verServicios(Model model) {
		List<ServicioDto> servicios = servicioService.findAllByOrderByNombreAsc();
		model.addAttribute("servicios", servicios);
		return ViewRouteHelper.CLIENTE_SERVICIOS;
	}
	
	@GetMapping("/servicios/{id}/lugares")
	public String verLugaresServicio(@PathVariable("id") Long servicioId, Model model) {
		ServicioDto servicio = servicioService.findById(servicioId);
		List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(servicioId);
		model.addAttribute("servicio", servicio);
		model.addAttribute("lugares", lugares);	    
		return ViewRouteHelper.CLIENTE_SERVICIOS_LUGARES;
	}
	
	@GetMapping("/lugares")
	public String verLugares(Model model) {
		List<LugarDto> lugares = lugarService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		List<Provincia> provincias = provinciaService.findAll();
		
		Map<Long, String> provinciasMap = provincias.stream()
			.collect(Collectors.toMap(Provincia::getId, Provincia::getNombre));
		
		Map<Long, String> localidadesMap = localidades.stream()
			.collect(Collectors.toMap(Localidad::getId, Localidad::getNombre));
		
		model.addAttribute("lugares", lugares);
		model.addAttribute("provinciasMap", provinciasMap);
		model.addAttribute("localidadesMap", localidadesMap);
		return ViewRouteHelper.CLIENTE_LUGARES;
	}
	
	@GetMapping("/lugar/{id}/servicios")
	public String verServiciosLugar(@PathVariable Long id, Model model) {
		LugarDto lugar = lugarService.findById(id);
		List<Servicio> servicios = servicioService.traerServiciosPorLugar(id);
		model.addAttribute("lugar", lugar);
		model.addAttribute("servicios", servicios);
		return ViewRouteHelper.CLIENTE_LUGARES_SERVICIOS;
	}
	
	@GetMapping("/eliminar-cuenta")
	public String eliminarCuenta() {
		return "/cliente/eliminar-cuenta";
	}
	
	@PostMapping("/eliminar-cuenta")
	public String eliminarCuentaPost (@RequestParam String email,
			@RequestParam String password, @RequestParam int dni) {
		// Elimina la cuenta del cliente
		clienteService.eliminarCuenta(email, password, dni);
		SecurityContextHolder.clearContext();
		return "redirect:/auth/login?logout";
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
	
	@GetMapping("/turnos")
    public String turnos(Model model, Principal principal) {
    	String email = principal.getName();
    	ClienteDto cliente = clienteService.findByEmail(email);
        Long clienteId = cliente.getId();
        List<TurnoDto> turnos = turnoService.buscarTurnosPorClienteId(clienteId);
        model.addAttribute("turnos", turnos);
        return "cliente/turnos"; 
    }
	
	@GetMapping("/detalle/{id}")
    public String detalleTurno(@PathVariable long id, Model model) {
        TurnoDto turno = turnoService.findById(id);
        model.addAttribute("turno", turno);
        return "cliente/detalle-turno";
    }
	
	@GetMapping("/reprogramar-turno/{id}")
	public String reprogramarTurno(@PathVariable long id, Model model) {
		TurnoDto turno = turnoService.findById(id);
		List<DisponibilidadDto> disponibilidades = disponibilidadService.obtenerDisponibilidadesPorProfesionalLibres(turno.getProfesional().getId());
		model.addAttribute("turno", turno);
		model.addAttribute("disponibilidades", disponibilidades);
		return "cliente/reprogramar-turno";
	}
	
	@PostMapping("/reprogramar-turno-post/{id}")
	public String reprogramarTurno(@PathVariable long id, @RequestParam("nuevaDisponibilidadId") long nuevaDisponibilidad) {
		boolean exito = turnoService.reprogramarTurno(id, nuevaDisponibilidad);
		if(!exito) {
			return "cliente/reprogramar-turno";
		}
		return "redirect:/cliente/turnos?reprogramado=ok";
	}
    
    @GetMapping("/eliminar/{id}")
    public String eliminarTurno(@PathVariable Long id) {
        turnoService.deleteById(id);
        return "redirect:/cliente/turnos?eliminado=ok"; 
    }
    
    @GetMapping("/cambiar-contraseña")
	public String cambiarContrasena(Model model) {
		model.addAttribute("cambioPasswordDto", new CambioPasswordDto());
		return "cliente/cambiar-contraseña";
	}
	
	@PostMapping("/cambiar-contraseña")
	public String cambiarContrasenaPost(@Valid @ModelAttribute CambioPasswordDto cambioPasswordDto,
			BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
			return "cliente/cambiar-contraseña";
		}
		ClienteDto cliente = clienteService.findByEmail(principal.getName());
		clienteService.cambiarContrasena(cliente, cambioPasswordDto);
		return "redirect:/cliente/perfil?cambioContrasena=ok";
	}
		
}
