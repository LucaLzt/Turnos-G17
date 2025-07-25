package com.oo2.grupo17.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.GenerarDisponibilidadDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.records.GenerarDisponibilidadesRecordDto;
import com.oo2.grupo17.dtos.records.ProfesionalRequestDto;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IProfesionalService;

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
@RequestMapping("/api/admin")
@SecurityRequirement(name = "basicAuth")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin API", description = "API para la gestión de funcionalidades administrativas del sistema de turnos")
public class AdminRestController {
	
	private final IProfesionalService profesionalService;
	
	/*
	 *  Se realizará la implementación de los métodos de la API REST para el administrador.
	 *  * Los métodos incluirán:
	 *  	- Modificar Profesionales
	 *  	- Eliminar Profesionales
	 *  	- Generar Disponibilidades
	 */
	
	@PutMapping("/modificarProfesional")
	@Operation(
			summary = "Modificar un profesional existente",
			description = "Permite al administrador modificar los detalles de un profesional, como su especialidad, disponibilidad y datos de contacto."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Profesional modificado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Profesional modificado exitosamente.")
					)
			),
			@ApiResponse(
					responseCode = "400", 
					description = "Error en los datos del profesional",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos del profesional: {mensaje de error}")
					)
			),
			@ApiResponse(
		            responseCode = "401",
		            description = "Usuario no autenticado",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Unauthorized",
			                          "message": "Credenciales inválidas. Verifica tu usuario y contraseña.",
			                          "status": 401,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/modificarProfesional",
			                          "user": "anonymous"
			                        }
			                        """
			                )
		            )
	        ),
		        @ApiResponse(
		            responseCode = "403",
		            description = "Acceso denegado - No tienes rol de ADMIN",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Forbidden",
			                          "message": "Acceso denegado: No tienes permisos para realizar esta operación.",
			                          "status": 403,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/modificarProfesional",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404", 
					description = "Profesional no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Profesional no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar el profesional: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> modificarProfesional(@RequestParam Long profesionalId,
			@Valid @RequestBody ProfesionalRequestDto profesionalDto,
			BindingResult result) {
		
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body("Error en los datos del profesional: " + result.getAllErrors());
		}
		
		try {
			ProfesionalDto profesionalNuevo = profesionalService.findById(profesionalId);
			profesionalService.update(profesionalId, profesionalNuevo);
			return ResponseEntity.ok("Profesional modificado exitosamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Profesional no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al modificar el profesional: " + e.getMessage());
		}
		
	}
	
	@DeleteMapping("/eliminarProfesional")
	@Operation(
			summary = "Eliminar un profesional existente",
			description = "Permite al administrador eliminar un profesional del sistema, liberando sus turnos y disponibilidades."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Profesional eliminado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Profesional eliminado exitosamente.")
					)
			),
			@ApiResponse(
		            responseCode = "401",
		            description = "Usuario no autenticado",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Unauthorized",
			                          "message": "Credenciales inválidas. Verifica tu usuario y contraseña.",
			                          "status": 401,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/eliminarProfesional",
			                          "user": "anonymous"
			                        }
			                        """
			                )
		            )
	        ),
		        @ApiResponse(
		            responseCode = "403",
		            description = "Acceso denegado - No tienes rol de ADMIN",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Forbidden",
			                          "message": "Acceso denegado: No tienes permisos para realizar esta operación.",
			                          "status": 403,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/eliminarProfesional",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404", 
					description = "Profesional no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Profesional no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al eliminar el profesional: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> eliminarProfesional(@RequestParam Long profesionalId) {
		try {
			profesionalService.eliminarProfesional(profesionalId);
			return ResponseEntity.ok("Profesional eliminado exitosamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Profesional no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al eliminar el profesional: " + e.getMessage());
		}
	}
	
	@PostMapping("/generarDisponibilidad")
	@Operation(
			summary = "Generar disponibilidades para un profesional",
			description = "Permite al administrador generar disponibilidades para un profesional, especificando fechas y horas disponibles."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Disponibilidades generadas exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Disponibilidades generadas exitosamente.")
					)
			),
			@ApiResponse(
		            responseCode = "401",
		            description = "Usuario no autenticado",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Unauthorized",
			                          "message": "Credenciales inválidas. Verifica tu usuario y contraseña.",
			                          "status": 401,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/generarDisponibilidad",
			                          "user": "anonymous"
			                        }
			                        """
			                )
		            )
	        ),
		        @ApiResponse(
		            responseCode = "403",
		            description = "Acceso denegado - No tienes rol de ADMIN",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Forbidden",
			                          "message": "Acceso denegado: No tienes permisos para realizar esta operación.",
			                          "status": 403,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/admin/generarDisponibilidad",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404", 
					description = "Profesional no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Profesional no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "422", 
					description = "Error en los datos de disponibilidad",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al generar disponibilidades: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> generarDisponibilidad(
			@Valid @RequestBody GenerarDisponibilidadesRecordDto generarDisponibilidadDto,
			BindingResult result) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de ingresados: " 
					+ result.getAllErrors());
		}
		
		try {
			
			GenerarDisponibilidadDto genDispDto = new GenerarDisponibilidadDto(
					generarDisponibilidadDto.profesionalId(),
					generarDisponibilidadDto.horaInicio(),
					generarDisponibilidadDto.horaFin(),
					generarDisponibilidadDto.duracionMinutos(),
					generarDisponibilidadDto.fechaInicio(),
					generarDisponibilidadDto.fechaFin()
			);
			
			profesionalService.generarDisponibilidadesAutomaticas(genDispDto);
			return ResponseEntity.ok("Disponibilidades generadas exitosamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Profesional no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al generar disponibilidades: " + e.getMessage());
		}
		
	}
	
}
