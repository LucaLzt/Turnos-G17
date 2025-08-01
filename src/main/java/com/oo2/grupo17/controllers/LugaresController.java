package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.Map;
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

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProvinciaService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/lugares")
public class LugaresController {
	
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;
	private final IDireccionService direccionService;
	private final ILugarService lugarService;

	@GetMapping("/agregar")
	public String agregarLugar(Model model) {
		model.addAttribute("lugar", new LugarDto());
		model.addAttribute("provincias", provinciaService.findAll());
        model.addAttribute("localidades", localidadService.findAll());
		return ViewRouteHelper.LUGARES_AGREGAR;
	}
	
	@PostMapping("/agregar")
	public String agregarLugarPost(@Valid @ModelAttribute("lugar") LugarDto lugarDto,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("provincias", provinciaService.findAll());
	        model.addAttribute("localidades", localidadService.findAll());
			return ViewRouteHelper.LUGARES_AGREGAR;
		}
		direccionService.crearDireccion(lugarDto, lugarDto.getDireccion());
		return "redirect:/admin/administrar-lugares?agregado=ok";
	}
	
	@GetMapping("/modificar")
	public String modificarLugar(Model model) {
		// 1. Cargo todos los lugares para mostrarlos en la vista
		List<LugarDto> lugares = lugarService.findAll();
		model.addAttribute("lugares", lugares);
		
		// 2. Cargo un map de provincias y localidades para el formulario
		List<Localidad> localidades = localidadService.findAll();
		List<Provincia> provincias = provinciaService.findAll();

		Map<Long, String> provinciasMap = provincias.stream()
			.collect(Collectors.toMap(Provincia::getId, Provincia::getNombre));
		
		Map<Long, String> localidadesMap = localidades.stream()
			.collect(Collectors.toMap(Localidad::getId, Localidad::getNombre));
		
		model.addAttribute("provinciasMap", provinciasMap);
		model.addAttribute("localidadesMap", localidadesMap);
		
		return ViewRouteHelper.LUGARES_LISTA_MODIFICAR;
	}
	
	@GetMapping("/eliminar")
	public String eliminarLugar(Model model) {
		// 1. Cargo todos los lugares para mostrarlos en la vista
		List<LugarDto> lugares = lugarService.findAll();
		model.addAttribute("lugares", lugares);
		
		// 2. Cargo un map de cantidad de turnos por lugar
		Map<Long, Long> turnosPorLugar = lugarService.getCantidadTurnosPorLugar();
		model.addAttribute("turnosPorLugar", turnosPorLugar);
		
		// 3. Cargo un map de provincias y localidades para el formulario
		List<Localidad> localidades = localidadService.findAll();
		List<Provincia> provincias = provinciaService.findAll();

		Map<Long, String> provinciasMap = provincias.stream()
			.collect(Collectors.toMap(Provincia::getId, Provincia::getNombre));
		
		Map<Long, String> localidadesMap = localidades.stream()
			.collect(Collectors.toMap(Localidad::getId, Localidad::getNombre));
		
		model.addAttribute("provinciasMap", provinciasMap);
		model.addAttribute("localidadesMap", localidadesMap);
		
		return ViewRouteHelper.LUGARES_LISTA_ELIMINAR;
	}
	
	@PostMapping("/{id}/eliminar")
	public String eliminarLugar(@PathVariable Long id, Model model) {
		lugarService.deleteById(id);
		return "redirect:/lugares/eliminar?eliminado=ok";
	}
	
	@GetMapping("/{id}/modificar")
	public String modificarLugar(@ModelAttribute("id") Long id, Model model) {
		// 1. Cargo el lugar a modificar
		LugarDto lugar = lugarService.findById(id);
		
		// 2. Cargo las provincias y localidades para el formulario
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		
		// 3. Cargo los atributos al modelo
		model.addAttribute("lugar", lugar);
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.LUGARES_MODIFICAR;
	}
	
	@PostMapping("/{id}/modificar")
	public String modificarLugarPost(@Valid @ModelAttribute("lugar") LugarDto lugarDto,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("provincias", provinciaService.findAll());
	        model.addAttribute("localidades", localidadService.findAll());
			return ViewRouteHelper.LUGARES_MODIFICAR;
		}
		direccionService.actualizarDireccion(lugarDto, lugarDto.getDireccion());
		return "redirect:/lugares/modificar?modificado=ok";
	}
	
	@GetMapping("/volver")
	public String volver() {
		return ViewRouteHelper.HOME_INDEX;
	}
	
}
