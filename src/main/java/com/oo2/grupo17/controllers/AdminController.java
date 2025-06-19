package com.oo2.grupo17.controllers;

import java.util.List;
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

import com.oo2.grupo17.dtos.DatosContactoDto;
import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.GenerarDisponibilidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IEmailService;
import com.oo2.grupo17.services.IEspecialidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {
	
	private final IProfesionalService profesionalService;
	private final IEspecialidadService especialidadService;
	private final IServicioService servicioService;
	private final ITurnoService turnoService;
	private final ILugarService lugarService;
	private final IEmailService emailService;

	@GetMapping("/registrar-profesional")
	public String registrarProfesional(Model model) {
		model.addAttribute("profesional", new ProfesionalRegistradoDto());
		return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
	}
	
	@PostMapping("/registrar-profesional")
	public String registrarProfesionalPost(@Valid @ModelAttribute("profesional") ProfesionalRegistradoDto profesionalDto,
			BindingResult result) {
		if(result.hasErrors()) {
			return ViewRouteHelper.ADMIN_REGISTRAR_PROFESIONAL;
		}
		profesionalService.registrarProfesional(profesionalDto);
		return "redirect:/index?registroExitoso";
	}
	
	@GetMapping("/administrar-servicios")
	public String administrarServicios() {
		return ViewRouteHelper.ADMIN_SERVICIOS;
	}
	
	@GetMapping("/administrar-lugares")
	public String administrarLugares() {
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
	@GetMapping("/administrar-profesional")
	public String administrarProfesional() {
		return ViewRouteHelper.ADMIN_PROFESIONAL;
	}
	
	@GetMapping("/administrar-especialidades")
	public String administrarEspecialidades() {
		return ViewRouteHelper.ADMIN_ESPECIALIDADES;
	}
	
	@GetMapping("/administrar-turnos")
	public String administrarTurnos(Model model) {
		List<TurnoDto> turnos = turnoService.findAll();
		model.addAttribute("turnos", turnos);
		return ViewRouteHelper.ADMIN_TURNOS;
	}
	
	@GetMapping("/turnos/{id}/modificar-turnos")
	public String modificarTurnos(@PathVariable("id") Long id, Model model) {
		TurnoDto turno= turnoService.findById(id);
		List<Lugar> lugares = lugarService.obtenerLugaresPorServicio(turno.getServicio().getId());
		model.addAttribute("turno", turno);
		model.addAttribute("lugares", lugares);
		return ViewRouteHelper.TURNO_MODIFICAR_TURNO;
	}
	
	@PostMapping("/turnos/{id}/modificar-turnos")
	public String modificarTurnosPost(@PathVariable("id") Long id,@ModelAttribute("turno") TurnoDto turno,
			@RequestParam("lugarId") Long lugarId) {
		LugarDto lugar = lugarService.findById(lugarId);
		turno.setLugar(lugar);
		turnoService.update(id,turno);
		return "redirect:/admin/administrar-turnos?turnoModificado=ok";
	}
	
	@GetMapping("/profesionales/generar-disponibilidades")
	public String generarDisponibilidades(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("datosFormulario", new GenerarDisponibilidadDto());
		return ViewRouteHelper.PROFESIONALES_DISPONIBILIDADES;
	}
	
	@PostMapping("/profesionales/generar-disponibilidades")
	public String generarDisponibilidadesPost(@Valid @ModelAttribute("datosFormulario") GenerarDisponibilidadDto dto,
			BindingResult result, Model model) {
		if(result.hasErrors()) {
			List<ProfesionalDto> profesionales = profesionalService.findAll();
			model.addAttribute("profesionales", profesionales);
			return ViewRouteHelper.PROFESIONALES_DISPONIBILIDADES;
		}
		profesionalService.generarDisponibilidadesAutomaticas(dto);
		return "redirect:/admin/administrar-profesional?disponibilidadesGeneradas=ok";
	}
	
	@GetMapping("/contactar-profesional")
	public String contactarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("datosContacto", new DatosContactoDto());
		return ViewRouteHelper.ADMIN_CONTACTAR_PROFESIONAL;
	}
	
	@PostMapping("/contactar-profesional")
	public String contactarProfesionalPost(@Valid @ModelAttribute("datosContacto") DatosContactoDto dto,
			BindingResult result, Model model) {
		if(result.hasErrors()) {
			List<ProfesionalDto> profesionales = profesionalService.findAll();
			model.addAttribute("profesionales", profesionales);
			return ViewRouteHelper.ADMIN_CONTACTAR_PROFESIONAL;
		}
		ProfesionalDto profesional = profesionalService.findById(dto.getProfesionalId());
		emailService.enviarEmail(profesional.getContacto().getEmail(), dto.getAsunto(), dto.getMensaje());
		return "redirect:/index?mensajeEnviado=ok";
	}
	
	@GetMapping("/profesionales/eliminar")
	public String eliminarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		return ViewRouteHelper.PROFESIONALES_LISTA_ELIMINAR;
	}
	
	@PostMapping("/profesionales/{id}/eliminar")
	public String eliminarProfesional(@PathVariable("id") Long id) {
		profesionalService.eliminarProfesional(id);
		return "redirect:/admin/profesionales/eliminar?eliminado=ok";
	}
	
	@GetMapping("/profesionales/modificar")
	public String modificarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		model.addAttribute("profesionales", profesionales);
		return ViewRouteHelper.PROFESIONALES_LISTA_MODIFICAR;
	}
	
	@GetMapping("/profesionales/{id}/modificar")
	public String modificarProfesional(@PathVariable("id") Long id, Model model) {
		ProfesionalDto profesional = profesionalService.findById(id);
		model.addAttribute("profesional", profesional);
		return ViewRouteHelper.PROFESIONALES_MODIFICAR;
	}
	
	@PostMapping("/profesionales/{id}/modificar")
	public String modificarProfesionalPost(@PathVariable("id") Long id, 
			@Valid @ModelAttribute("profesional") ProfesionalDto profesional, BindingResult result) {
		if(result.hasErrors()) {
			return 	ViewRouteHelper.PROFESIONALES_MODIFICAR;
		}
		profesionalService.update(id, profesional);
		return "redirect:/admin/profesionales/modificar?modificado=ok";
	}
	
	@GetMapping("/profesionales/gestion")
	public String gestionarProfesional(Model model) {
		List<ProfesionalDto> profesionales = profesionalService.findAll();
		List<ServicioDto> servicios = servicioService.findAll();
		List<LugarDto> lugares = lugarService.findAll();
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("profesionales", profesionales);
		model.addAttribute("servicios", servicios);
		model.addAttribute("lugares", lugares);
		model.addAttribute("especialidades", especialidades);		
		return ViewRouteHelper.PROFESIONALES_LISTA_GESTION;
	}
	
	@GetMapping("/profesionales/{id}/gestion")
	public String gestionarProfesional(@PathVariable("id") Long id, Model model) {
		ProfesionalDto profesional = profesionalService.findById(id);
		List<ServicioDto> servicios = servicioService.findAll();
		List<LugarDto> lugares = lugarService.findAll();
		List<EspecialidadDto> especialidades = especialidadService.findAll();
		model.addAttribute("profesional", profesional);
		model.addAttribute("servicios", servicios);
		model.addAttribute("lugares", lugares);
		model.addAttribute("especialidades", especialidades);
		return ViewRouteHelper.PROFESIONALES_GESTION;
	}
	
	@PostMapping("/profesionales/{id}/gestion")
	public String gestionarProfesionalPost(@PathVariable("id") Long id, @ModelAttribute("profesional") ProfesionalDto profesional
			, @RequestParam(value = "serviciosIds", required = false) List<Long> serviciosIds) {
		profesional.setServiciosIds(serviciosIds);
		profesionalService.asignarDatosProfesional(id, profesional, serviciosIds);
		return "redirect:/admin/profesionales/gestion?gestionado=ok";
	}

	@ResponseBody
	@GetMapping("/profesionales/servicios-por-lugar/{lugarId}")
	public List<ServicioDto> getServiciosPorLugar(@PathVariable Long lugarId){
		List<Servicio> servicios = servicioService.traerServiciosPorLugar(lugarId);
		return servicios.stream()
				.map(serv -> new ServicioDto(serv.getId(), serv.getNombre(), serv.getDescripcion(), serv.getPrecio(), serv.getLugares().stream().map(Lugar::getId).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

}
