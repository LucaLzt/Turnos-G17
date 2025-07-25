package com.oo2.grupo17.controllers.api;

import java.util.List;

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


import com.oo2.grupo17.dtos.EspecialidadDto;
import com.oo2.grupo17.dtos.records.EspecialidadRequestDto;
import com.oo2.grupo17.dtos.records.EspecialidadResponseDto;

import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;

import com.oo2.grupo17.services.IEspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;


@RestController @Builder
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades API", description = "API para gestionar especialidades")
public class EspecialidadRestController {

    private final IEspecialidadService especialidadService;

	/*
	 * 	Se realizará la implementación de los métodos de la API REST para gestionar especialidades.
	 *  * Los métodos estarán protegidos por autenticación y autorización.
	 * 	* Los métodos incluirán:
	 * 		- Agregar una nueva Especialidad.
	 * 		- Modificar una especialidad.
	 * 		- Eliminar una especialidad.
	 * 		
	 * 	* Se creará records para representar los datos de Especialiad
	 */

	@PostMapping("/agregarEspecialidad")
	@SecurityRequirement(name = "basicAuth")
    @Operation(
    		summary = "Agregar una nueva especialidad",
    		description = "Agrega una nueva especialidad disponible para los profesionales. **Privado ADMIN**"	
    )
    @ApiResponses(value = {
    		@ApiResponse(
    				responseCode = "201",
		    		description = "Especialidad creada exitosamente.",
		    		content = @Content(
		    				mediaType= "text/plain",
		    				schema = @Schema(type = "string", example= "Especialidad creada exitosamente.")
		    		)
			),
			@ApiResponse(
					responseCode = "409",
					description = "La especialidad ya existe.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "La especialidad ya existe: {errorMessage}")
					)
		    ),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos de la especialidad.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos de la especialidad: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al crear la especialidad: {errorMessage}")
					)
			)     
    })
    public ResponseEntity<String> agregarEspecialidad(
    		@Valid @RequestBody EspecialidadRequestDto especialidadDto,
    		BindingResult result) {
    	
    	if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de la especialidad: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
    	
        try {
            EspecialidadDto nuevaEspecialidad = new EspecialidadDto ( 
            		null, // ID se generará automáticamente
            		especialidadDto.nombre()
            		);
            especialidadService.save(nuevaEspecialidad);
            return ResponseEntity.status(201).body("Especialidad creada exitosamente.");
        } catch (EntidadDuplicadaException e) {
            return ResponseEntity.status(409).body("Ya existe una especialidad: "+ e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear especialidad: " + e.getMessage());
        }
    }
	
	@GetMapping("/obtenerTodos")
    @Operation(
    		summary = "Listar todas las especialidades",
    		description = "Obtiene una lista de todas las especialidades registradas. **Público**"
    )
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de especialidades obtenida exitosamente.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = EspecialidadResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al listar las especialidades: {errorMessage}")
					)
			)	
	})
    public ResponseEntity<?> listarEspecialidades() {
		try {
			List<EspecialidadDto> especialidades = especialidadService.findAll();
        	List<EspecialidadResponseDto> especialidadesResponses= especialidades.stream()
        		.map(especialidad ->  new EspecialidadResponseDto(
        				especialidad.getId(),
        				especialidad.getNombre())
        		)
        		.toList();
        	return ResponseEntity.ok(especialidadesResponses);
        } catch (Exception e) {
    	 	return ResponseEntity.status(500).body("Error al listar las especialidades: " + e.getMessage());
    	}		
	}
	
	@GetMapping("/obtener/{id}")
    @Operation(
    		summary = "Obtener una especialidad por ID",
    		description = "Obtiene los detalles de una Especialidad específica por su ID. **Público**"
    )
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Especialidad obtenida exitosamente.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = EspecialidadResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Especialiad no encontrada.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Especialidad no encontrada.")
					)
			),
          
    })
    public ResponseEntity<?> obtenerEspecialidadPorId(@PathVariable Long id) {
        try {
            EspecialidadDto especialidad = especialidadService.findById(id);
            EspecialidadResponseDto especialidadResponse = new EspecialidadResponseDto(
            		especialidad.getId(),
            		especialidad.getNombre());
            return ResponseEntity.ok(especialidadResponse);
        } catch (EntidadNoEncontradaException e) {
            return ResponseEntity.status(404).body("Especialidad no encontrada.");
        }
    	 catch (Exception e) {
		return ResponseEntity.status(500).body("Error al obtener la especialidad: " + e.getMessage());
    	}
    }
    
	@PutMapping("/actualizar/{id}")
	@SecurityRequirement(name = "basicAuth")
    @Operation(
    		summary = "Actualizar una Especialidad",
			description = "Actualiza los detalles de una Especialidad existente por su ID. **Privado ADMIN**"
	)
    @ApiResponses(value = {
    		@ApiResponse(
					responseCode = "200",
					description = "Especialidad actualizada exitosamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Especialidad actualizada exitosamente.")
					)
			),
    		@ApiResponse(
					responseCode = "404",
					description = "Especialidad no encontrada.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Especialidad no encontrada.")
					)
			),
    		@ApiResponse(
					responseCode = "422",
					description = "Error en los datos de la especialidad.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos de la especialidad: {errorMessage}")
					)
			),
    		@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al actualizar la especialidad: {errorMessage}")
					)
			)
    		
    })
    public ResponseEntity<String> actualizarEspecialidad(
    		@PathVariable Long id,
    		@Valid @RequestBody EspecialidadRequestDto especialidadDto,
    		BindingResult result) {
    	if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de la especialidad: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
    	
        try {
        	EspecialidadDto especialidadNueva = new EspecialidadDto(
        			null,
        			especialidadDto.nombre()
        			);
        	
           especialidadService.update(id, especialidadNueva);
            
            return ResponseEntity.ok("Especialidad actualizada exitosamente.");
        } catch (EntidadNoEncontradaException e) {
            return ResponseEntity.status(404).body("Especialidad no encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar especialidad: " + e.getMessage());
        }
    }

	@DeleteMapping("/eliminar/{id}")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Eliminar una Especialidad",
			description = "Elimina una Especialidad existente por su ID. **Privado ADMIN**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Especialidad eliminada exitosamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Especialidad eliminada exitosamente.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Especialidad no encontrada.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Especialiad no encontrada: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al eliminar la especialidad: {errorMessage}")
					)
			)
	})
    public ResponseEntity<String> eliminarEspecialidad(@PathVariable Long id) {
        try {
           especialidadService.deleteById(id);
           return ResponseEntity.ok("Especialidad eliminada exitosamente.");
        } catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Especialidad no encontrada: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al eliminar la especialidad: " + e.getMessage());
		}
    }
	
}
