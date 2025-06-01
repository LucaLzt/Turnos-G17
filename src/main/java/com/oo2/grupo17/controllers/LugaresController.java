package com.oo2.grupo17.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.LocalidadDto;
import com.oo2.grupo17.dtos.ProvinciaDto;
import com.oo2.grupo17.entities.Localidad;
import com.oo2.grupo17.entities.Provincia;
import com.oo2.grupo17.helpers.ViewRouteHelper;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.ILocalidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProvinciaService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/lugares")
public class LugaresController {
	
	private final IProvinciaService provinciaService;
	private final ILocalidadService localidadService;
	private final IDireccionService direccionService;
	private final ILugarService lugarService;

	@GetMapping("/agregar")
	public String agregarLugar(Model model) {
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		model.addAttribute("lugar", new LugarDto());
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.LUGARES_AGREGAR;
	}
	
	@PostMapping("/agregar")
	public String agregarLugarPost(@ModelAttribute("lugar") LugarDto lugarDto,
			BindingResult result) {
		if (result.hasErrors()) {
			return ViewRouteHelper.LUGARES_AGREGAR;
		}
		direccionService.crearDireccion(lugarDto, lugarDto.getDireccion());	
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
	@GetMapping("/modificar")
	public String modificarLugar(Model model) {
		// Cargo todos los lugares para mostrarlos en la vista
		List<LugarDto> lugares = lugarService.findAll();
		model.addAttribute("lugares", lugares);
		
		
		// Cargo un map de provincias y localidades para el formulario
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
		// Cargo todos los lugares para mostrarlos en la vista
		List<LugarDto> lugares = lugarService.findAll();
		model.addAttribute("lugares", lugares);
		
		
		// Cargo un map de provincias y localidades para el formulario
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
	
	@GetMapping("/{id}/modificar")
	public String modificarLugar(@ModelAttribute("id") Long id, Model model) {
		LugarDto lugar = lugarService.findById(id);
		model.addAttribute("lugar", lugar);
		List<Provincia> provincias = provinciaService.findAll();
		List<Localidad> localidades = localidadService.findAll();
		model.addAttribute("provincias", provincias);
		model.addAttribute("localidades", localidades);
		return ViewRouteHelper.LUGARES_MODIFICAR;
	}
	
	@PostMapping("/{id}/modificar")
	public String modificarLugarPost(@ModelAttribute("lugar") LugarDto lugarDto,
			BindingResult result) {
		if (result.hasErrors()) {
			return ViewRouteHelper.LUGARES_AGREGAR;
		}
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		System.out.println("LUGAR ID: " + lugarDto.getId());
		
		direccionService.actualizarDireccion(lugarDto, lugarDto.getDireccion());
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
	@GetMapping("/{id}/eliminar")
	public String eliminarLugar(@ModelAttribute("id") Long id, Model model) {
		lugarService.deleteById(id);
		return ViewRouteHelper.ADMIN_LUGARES;
	}
	
}
