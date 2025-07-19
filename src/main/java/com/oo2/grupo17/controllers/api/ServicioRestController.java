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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.records.ServicioGetDto;
import com.oo2.grupo17.dtos.records.ServicioPostDto;
import com.oo2.grupo17.dtos.records.ServicioPutDto;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IServicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;

@RestController @Builder
@RequestMapping("api/servicio")
@Tag(name = "Servicio", description = "Operaciones sobre Servicio")
public class ServicioRestController {
	
	private IServicioService servicioService;
	
	@GetMapping("/{id}/traer")
	@Operation(summary = "Traer servicio", description = "Se trae un Servicio por ID")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "Servicio encontrado."),
		@ApiResponse(responseCode = "404", description = "Servicio no encontrado."),
		@ApiResponse(responseCode = "500", description = "Probremas en el sistema.")
	})
	public ResponseEntity<?> getServicio(@PathVariable Long id){
		try {
		ServicioDto dto = servicioService.findById(id);
		ServicioGetDto getDto = new ServicioGetDto(dto.getId(), 
				dto.getNombre(), 
				dto.getDescripcion(), 
				dto.getPrecio(), 
				dto.getLugaresIds());
		return ResponseEntity.ok(getDto);
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al traer el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema.");
		}
	}
	
	@GetMapping("/traer")
	@Operation(summary = "Traer todos los Servicios", description = "Se traen todos los servicios.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicios encontrados."),
			@ApiResponse(responseCode = "500", description = "Problemas en el Sistema.")
	})
	public ResponseEntity<?> traerServicios() {
		try {
			List<ServicioDto> getsDto = servicioService.findAll();
			return ResponseEntity.status(200).body(getsDto);
		} catch (Exception e){
			return ResponseEntity.status(500).body("Error en el Sistema.");
		}
	}
	
	@PostMapping("/agregar")
	@Operation(summary = "Agregar Servicio", description = "Se agrega un Servicio")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicio agregado."),
			@ApiResponse(responseCode = "422", description = "Datos inválidos."),
			@ApiResponse(responseCode = "409", description = "Servicio ya creado."),
			@ApiResponse(responseCode = "500", description = "Problemas en el sistema.")
	})
	public ResponseEntity<String> agregarServicio(@Valid @RequestBody ServicioPostDto postDto, BindingResult results){
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
		
		try {
			ServicioDto dto = new ServicioDto(postDto.nombre(), postDto.descripcion(), postDto.precio(), new ArrayList<>());
			servicioService.save(dto);
			return ResponseEntity.status(200).body("Servicio agregado correctamente.");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("Error al agregar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema.");
		}
		
	}
	
	@DeleteMapping("/{id}/eliminar")
	@Operation(summary = "Eliminar Servicio", description = "Se elimina un Servicio por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicio eliminado."),
			@ApiResponse(responseCode = "404", description = "Servicio no encontrado."),
			@ApiResponse(responseCode = "500", description = "Problemas en el sistema.")
	})
	public ResponseEntity<String> eliminarServicio(@PathVariable Long id){
		try {
			servicioService.deleteById(id);
			return ResponseEntity.status(200).body("Servicio eliminado correctamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al eliminar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema.");
		}

	}
	
	@PutMapping("/{id}/modificar")
	@Operation(summary = "Modificar Servicio", description = "Se modifica un Servicio por ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Servicio modificado."),
			@ApiResponse(responseCode = "404", description = "Servicio no encontrado."),
			@ApiResponse(responseCode = "409", description = "Servicio ya creado."),
			@ApiResponse(responseCode = "422", description = "Datos inválidos."),
			@ApiResponse(responseCode = "500", description = "Problemas en el sistema")
	})
	public ResponseEntity<String> modificarServicio(@PathVariable Long id, @RequestBody ServicioPutDto putDto, BindingResult results){
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
				try {
			ServicioDto dto = new ServicioDto(putDto.nombre(), putDto.descripcion(), putDto.precio(), putDto.idsLugares());
			servicioService.update(id, dto);
			return ResponseEntity.status(200).body("Servicio modificado correctamente.");
		} catch (EntidadNoEncontradaException e){
			return ResponseEntity.status(404).body("Error al modificar el Servicio: " + e.getMessage());
		} catch (EntidadDuplicadaException e){
			return ResponseEntity.status(409).body("Error al modificar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema.");
		}
	}
}
