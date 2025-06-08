package com.oo2.grupo17.controllers;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.dtos.LocalidadDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.entities.Profesional;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IEspecialidadService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IProvinciaService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import lombok.Builder;

@Controller 
@SessionAttributes("turno") 
@Builder
@RequestMapping("/cliente")

public class TurnosController {		private final IServicioService servicioService;
	    private final ILugarService lugarService;
		private final IClienteService clienteService;
		private final IContactoService contactoService;
		private final IDireccionService direccionService;
		private final IProvinciaService provinciaService;
		private final ILocalidadService localidadService;
		public final IProfesionalService profesionalService;
		public final IEspecialidadService especialidadService;
		public final ITurnoService turnoService;
		
		 //Usa la anotacion @SessionAttributes para mantener turno entre varia requests
	     @ModelAttribute("turno")
	    public TurnoDto inicializarTurno() {
		      return new TurnoDto(); // se inicializa una vez y se guarda en sesión
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
		public String modificarServicio(@PathVariable("id") Long id, Model model) {
			ServicioDto servicio = servicioService.findById(id);
			List<LugarDto> lugares = lugarService.findAll();
			model.addAttribute("servicio", servicio);
			model.addAttribute("lugares", lugares);
			return ViewRouteHelper.SERVICIOS_MODIFICAR;
		}
		


		@PreAuthorize("hasRole('ROLE_CLIENTE')")
		@GetMapping("/solicitar-turno")
		public String solicitarTurnos(Model model, Principal principal) {
			TurnoDto turno=new TurnoDto();
			// Obtiene el email del usuario autenticado y busco al cliente
			String email = principal.getName();
			ClienteDto cliente = clienteService.findByEmail(email);
			// Si el cliente existe, lo agrega al modelo
			if(cliente != null) {
				
				turno.setCliente(cliente);	
			}
			// Traigo todos los servicios disponibles
			List<ServicioDto> servicios = servicioService.findAllByOrderByNombreAsc();
			model.addAttribute("servicios", servicios);
			model.addAttribute("turno", turno );

		
			return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR;
		}
		
		@PreAuthorize("hasRole('ROLE_CLIENTE')")
		@GetMapping("/solicitar-turno/{id}/elegir-lugar")
		public String elegirServicio(@PathVariable("id") Long servicioId, Model model,@ModelAttribute("turno") TurnoDto Turno) {
			ServicioDto servicio = servicioService.findById(servicioId);
			List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(servicioId);
	
			Turno.setServicio(servicio);
			model.addAttribute("servicio", servicio);
			model.addAttribute("lugares", lugares);	  
			model.addAttribute("turno", Turno);
		
	
			return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR_LUGAR;
		}
		
		
		@PreAuthorize("hasRole('ROLE_CLIENTE')")
		@GetMapping("/solicitar-turno/{id}/elegir-profesional")
		public String elegirProfesional(@PathVariable("id") Long lugarId, Model model,@ModelAttribute("turno") TurnoDto Turno) {
			LugarDto lugar =lugarService.findById(lugarId);
			DireccionDto direccion=lugar.getDireccion();
			ProvinciaDto provincia= provinciaService.findById(direccion.getProvinciaId());
			LocalidadDto localidad = localidadService.findById(direccion.getLocalidadId());
			List <Profesional> profesionales = profesionalService.obtenerProfesionalesPorLugar(lugarId);
			model.addAttribute("lugar", lugar);
			model.addAttribute("provincia", provincia);
			model.addAttribute("localidad", localidad);
			
			//ServicioDto servicio = servicioService.findById(lugarId);
			//List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(lugarId);
	
		//	Turno.setServicio(servicio);
			//model.addAttribute("servicio", servicio);
			model.addAttribute("profesionales", profesionales);	  
			//model.addAttribute("turno", Turno);
		
	
			return ViewRouteHelper.TURNO_SOLICITUD_ELEGIR_PROFESIONAL;
		}
		
		
		

		

}
