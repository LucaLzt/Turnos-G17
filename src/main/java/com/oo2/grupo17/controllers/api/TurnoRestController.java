package com.oo2.grupo17.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.dtos.records.TurnoResponseDto;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;

@RestController @Builder
@RequestMapping("/api/turno")
@Tag(name = "Turno API", description = "API para gestionar turnos")
public class TurnoRestController {
	
	private ITurnoService turnoService;
	
	@GetMapping("/traer/{id}")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Traer un Turno", 
			description = "Se trae un Turno por ID. **Privado ADMIN-CLIENTE-PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Turno encontrado.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = TurnoResponseDto.class)
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
			                          "path": "/api/clientes/verDatosCliente",
			                          "user": "anonymous"
			                        }
			                        """
			                )
		            )
	        ),
			@ApiResponse(
		            responseCode = "403",
		            description = "Acceso denegado - No tienes rol de ADMIN-CLIENTE-PROFESIONAL",
		            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(
			                    example = """
			                        {
			                          "error": "Forbidden",
			                          "message": "Acceso denegado: No tienes permisos para realizar esta operación.",
			                          "status": 403,
			                          "timestamp": "2025-07-25T19:15:36Z",
			                          "path": "/api/clientes/verDatosCliente",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404", 
					description = "No se encontro el Turno.", 
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al traer Turno: {error_message}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el sistema", 
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el sistema: {error_message}")
					)
			)
	})
	public ResponseEntity<?> traerTurno(@PathVariable Long id) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		try {
			TurnoDto dto = turnoService.findById(id);
			
			// Solo puede ver el turno el Admin o el Cliente/Profesional asociado al turno.
			if (dto.getCliente().getContacto().getEmail().equals(email) || 
					dto.getProfesional().getContacto().getEmail().equals(email) || 
					email.equals("admin1234@admin.com")) {
				TurnoResponseDto responseDto = new TurnoResponseDto(
						dto.getId(), 
						dto.getCliente().getNombre(), 
						dto.getProfesional().getNombre(), 
						dto.getLugar().getDireccion().getCalle() + " " 
								+ dto.getLugar().getDireccion().getAltura() + ", " 
								+ dto.getLugar().getDireccion().getProvinciaId() + ", " 
								+ dto.getLugar().getDireccion().getLocalidadId(),
						dto.getServicio().getNombre(), 
						dto.getDisponibilidad().getInicio());
				return ResponseEntity.status(200).body(responseDto);
			} else {
				return ResponseEntity.status(403).body("El turno que intentas ver no pertenece al usuario actual.");
			}
			
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al traer Turno: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/traer")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Traer todos los Turnos", 
			description = "Se traen todos los Turnos. **Privado ADMIN**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Turnos encontrados.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = TurnoResponseDto.class)
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
			                          "path": "/api/clientes/verDatosCliente",
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
			                          "path": "/api/clientes/verDatosCliente",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {error_message}")
					)
			)
	})
	public ResponseEntity<?> traerTurnos(){
		try{
			List<TurnoDto> dto = turnoService.findAll();
			List<TurnoResponseDto> responseDto = new ArrayList<>();
			for(int i = 0; i < dto.size(); i++) {
				TurnoDto auxDto = dto.get(i);
				TurnoResponseDto auxGetDto = new TurnoResponseDto(auxDto.getId(), 
						auxDto.getCliente().getNombre(), 
						auxDto.getProfesional().getNombre(), 
						auxDto.getLugar().getDireccion().getCalle() + " " + auxDto.getLugar().getDireccion().getAltura() + ", " 
								+ auxDto.getLugar().getDireccion().getProvinciaId() + ", " 
								+ auxDto.getLugar().getDireccion().getLocalidadId(),
						auxDto.getServicio().getNombre(), 
						auxDto.getDisponibilidad().getInicio());
				responseDto.add(auxGetDto);
			}
			return ResponseEntity.status(200).body(responseDto);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
}
