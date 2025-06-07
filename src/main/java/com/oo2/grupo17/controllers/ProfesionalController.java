package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.repositories.IProfesionalRepository;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IEspecialidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IServicioService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/profesionales")
public class ProfesionalController {
	
	public final IProfesionalService profesionalService;
	public final IProfesionalRepository profesionalRepository;
	public final IServicioService servicioService;
	public final ILugarService lugarService;
	public final IDireccionService direccionService;
	public final IEspecialidadService especialidadService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar")
	public String eliminarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		return ViewRouteHelper.PROFESIONAL_LISTA_ELIMINAR;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/{id}/eliminar")
	public String eliminarProfesional(@PathVariable("id") Long id) {
		profesionalService.eliminarProfesional(id);
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
			@RequestParam(value = "serviciosId", required = false) Set<Long> serviciosId,
			@RequestParam(value = "lugarId") Long lugarId
			) {
		profesionalService.asignarDatosProfesional(id, especialidadId, lugarId, serviciosId);
		return "redirect:/profesionales/gestion?gestionado=ok";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/servicios-por-lugar/{lugarId}")
	@ResponseBody
	public List<ServicioDto> getServiciosPorLugar(@PathVariable Long lugarId){
		List<Servicio> servicios = servicioService.traerServiciosPorLugar(lugarId);
		return servicios.stream()
				.map(serv -> new ServicioDto(serv.getId(), serv.getNombre(), serv.getDescripcion(), serv.getPrecio(), serv.getLugares().stream().map(Lugar::getId).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
}
