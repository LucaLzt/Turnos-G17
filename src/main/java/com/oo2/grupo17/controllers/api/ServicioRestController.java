package com.oo2.grupo17.controllers.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.records.ServicioAgregarDto;
import com.oo2.grupo17.dtos.records.ServicioModificarDto;
import com.oo2.grupo17.dtos.records.ServicioRecordDto;
import com.oo2.grupo17.entities.Lugar;
import com.oo2.grupo17.entities.Servicio;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IServicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
@RestController
@Tag(name = "Servicio", description = "Operaciones sobre Servicios")
@RequestMapping("/api/servicio")
public class ServicioRestController {
	
	private final IServicioService servicioService;
	
	@PostMapping("/agregar")
	@Operation(summary = "Agregar nuevo Servicio", description = "Premite agregar un nuevo Servicio en el sistema")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Agregado de Servicio exitoso"),
			@ApiResponse(responseCode = "409", description = "Servicio repetido"),
			@ApiResponse(responseCode = "422", description = "Datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Problemas en el servidor")
	})
	public ResponseEntity<String> agregarServicio(@Valid @RequestBody ServicioAgregarDto servicioRestDto, BindingResult results){
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos del Servicio: \n" + errors);
		}
		try {
			ServicioDto servicio = new ServicioDto(
					servicioRestDto.nombre(), 
					servicioRestDto.descripcion(), 
					servicioRestDto.precio(), 
					new ArrayList<>()
					);
			servicioService.save(servicio);
			return ResponseEntity.status(200).body("Se agrego el Servicio correctamente.");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("Ya existe este Servicio");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al agregar el Servicio");
		}
		
	}
	
	@PostMapping("/{id}/modificar")
	@Operation(summary = "Modificar Servicio", description = "Se modifican los datos de un Servicio por id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Modificacion exitosa"),
			@ApiResponse(responseCode = "404", description = "Servicio a modificar no encontrado"),
			@ApiResponse(responseCode = "409", description = "Servicio repetido"),
			@ApiResponse(responseCode = "422", description = "Datos inválidos"),
			@ApiResponse(responseCode = "500", description = "Problemas en el Servidor")
	})
	public ResponseEntity<String> modificarServicio(@PathVariable Long id, @Valid @RequestBody ServicioModificarDto servicioRestDto, BindingResult results) {
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos del Servicio: \n" + errors);
		}
		try {
			ServicioDto servicio = servicioService.findById(id);
			servicio.setNombre(servicioRestDto.nombre());
			servicio.setDescripcion(servicioRestDto.descripcion());
			servicio.setPrecio(servicioRestDto.precio());
			servicio.setLugaresIds(servicioRestDto.idsLugares());
			servicioService.update(id, servicio);
			return ResponseEntity.status(200).body("Servicio modificado correctamente");
		} catch(EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Servicio a modificar no encontrado");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("Ya existe este Servicio");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al modificar el Servicio");
		}
	}
	
	@DeleteMapping("/{id}/eliminar")
	@Operation(summary = "Eliminar Servicio", description = "Se elimina un servicio por id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicio eliminado correctamente"),
			@ApiResponse(responseCode = "404", description = "Servicio a eliminar no encontrado"),
			@ApiResponse(responseCode = "500", description = "Problemas en el servidor")
	})
	public ResponseEntity<String> eliminarServicio(@PathVariable("id") Long id){
		try {
			servicioService.deleteById(id);
			return ResponseEntity.status(200).body("Servicio eliminado correctamente");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Servicio a eliminar no encontrado");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al eliminar Servicio");
		}
		
	}
	
	@GetMapping("/por-lugar/{idLugar}")
	@Operation(summary = "Traer Servicios de un Lugar", description = "Se traen los Servicio de un Lugar por su id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicios encontrados"),
			@ApiResponse(responseCode = "404", description = "Lugar no encontrado"),
			@ApiResponse(responseCode = "500", description = "Problemas en el servidor")
	})
	public ResponseEntity<?> getServiciosPorLugar(@PathVariable("idLugar") Long idLugar){
		try{
			List<Servicio> servicios = servicioService.traerServiciosPorLugar(idLugar);
		
			List<ServicioRecordDto> dto = servicios.stream().map(serv -> new ServicioRecordDto(
					serv.getId(),
					serv.getNombre(),
					serv.getDescripcion(),
					serv.getPrecio(),
					serv.getLugares().stream().map(Lugar::getId).collect(Collectors.toList()))
					).collect(Collectors.toList());
			return ResponseEntity.status(200).body(dto);
		} catch (EntidadNoEncontradaException e){
			return ResponseEntity.status(404).body("No se encontro el lugar");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al traer los Servicios de un Lugar");
		}
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Traer Servicio", description = "Se trae un servicio por id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicio encontrado"),
			@ApiResponse(responseCode = "404", description = "Servicio no encontrado")
	})
	public ResponseEntity<?> getServicio(@PathVariable("id") Long id){
		try {
	        ServicioDto servicio = servicioService.findById(id);
	        ServicioRecordDto dto = new ServicioRecordDto(servicio.getId(), servicio.getNombre(), servicio.getDescripcion(), servicio.getPrecio(), servicio.getLugaresIds());
	        return ResponseEntity.status(200).body(dto);
	    } catch (EntidadNoEncontradaException e) {
	        return ResponseEntity.status(404).body("Servicio no encontrado");
	    }		
	}
}