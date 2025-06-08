package com.oo2.grupo17.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Turno;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IEspecialidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/profesionales")
public class ProfesionalController {
	
	public final IProfesionalService profesionalService;
	public final IServicioService servicioService;
	public final ILugarService lugarService;
	public final IEspecialidadService especialidadService;
	public final ITurnoService turnoService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		return ViewRouteHelper.PROFESIONAL_LISTA_ELIMINAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/eliminar")
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
			@RequestParam(value = "especialidadId", required = false) Long especialidadId,
			@RequestParam(value = "serviciosId", required = false) Set<Long> serviciosId,
			@RequestParam(value = "lugarId", required = false) Long lugarId) {
		// if(especialidadId == null) {
		// 	System.out.println("adas");
		// }
		profesionalService.asignarDatosProfesional(id, especialidadId, lugarId, serviciosId);
		return "redirect:/profesionales/gestion?gestionado=ok";
	};
	
	@GetMapping("/home")
	public String index() {
		return ViewRouteHelper.HOME_INDEX;
	}
	
	@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
	@GetMapping("/cancelar-turno")
    public String cancelarTurnosProfesional(Model model, Principal principal) {
    	String email = principal.getName();
    	ProfesionalDto profesional = profesionalService.findByEmail(email);
        Long profesionalId = profesional.getId();
        

        List<Turno> turnos = turnoService.buscarTurnosPorProfesionalId(profesionalId);
        System.out.println(turnos.get(0).getId());
        model.addAttribute("turnos", turnos);

        return "profesionales/TurnosACancelar";
    }
	
	@GetMapping("/detalle/{id}")
    public String detalleTurno(@PathVariable("id") long id, Model model) {
        TurnoDto turno = turnoService.findById(id);
        model.addAttribute("turno", turno);
        return "profesionales/DetalleTurno";
    }
	
	@GetMapping("/eliminar/{id}")
    public String eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return "redirect:/profesionales/cancelar-turno"; 
    }
}
