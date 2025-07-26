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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.records.DireccionResponseDto;
import com.oo2.grupo17.dtos.records.LugarRequestDto;
import com.oo2.grupo17.dtos.records.LugarResponseDto;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.ILugarService;

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
@RequestMapping("/api/lugares")
@Tag(name = "Lugares API", description = "API para gestionar lugares")
public class LugaresRestController {
	
	private final ILugarService lugarService;
	private final IDireccionService direccionService;

	/*
	 *  Se realizará la implementación de los métodos de la API REST para gestionar lugares.
	 *  * Los métodos estarán protegidos por autenticación y autorización.
	 * 	* Los métodos incluirán:
	 * 		- Crear un nuevo lugar.
	 * 		- Actualizar un lugar existente.
	 * 		- Eliminar un lugar.
	 * 		- Listar todos los lugares.
	 * 		- Obtener un lugar por ID.
	 * 	* Se crearán dos records para representar los datos de un lugar y su dirección.
	 */
	
	
	@PostMapping("/crearLugar")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Crear un nuevo lugar",
			description = "Crea un nuevo lugar junto con su dirección asociada. **Privado ADMIN**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Lugar creado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar creado exitosamente")
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
			                          "path": "/api/lugares/crearLugar",
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
			                          "path": "/api/lugares/crearLugar",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "409",
					description = "El lugar ya existe",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "El lugar ya existe: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos del lugar",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos del lugar: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al crear lugar: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> crearLugar(
			@Valid @RequestBody LugarRequestDto lugarDto, 
			BindingResult result) {
		
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos del lugar: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			LugarDto nuevoLugar = new LugarDto(
					null, // ID se generará automáticamente
					new DireccionDto(
							null, // ID se generará automáticamente
							lugarDto.direccion().calle(),
							lugarDto.direccion().altura(),
							lugarDto.direccion().provinciaId(),
							lugarDto.direccion().localidadId()
					),
					lugarDto.horarioApertura(),
					lugarDto.horarioCierre()
			);
			direccionService.crearDireccion(nuevoLugar, nuevoLugar.getDireccion());
			return ResponseEntity.status(201).body("Lugar creado exitosamente");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("El lugar ya existe: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al crear lugar: " + e.getMessage());
		}
		
	}
	
	@GetMapping("/obtenerTodos")
	@Operation(
			summary = "Listar todos los lugares",
			description = "Obtiene una lista de todos los lugares registrados.  **Público**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lista de lugares obtenida exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = LugarResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al listar los lugares: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> listarLugares() {
		try {
			List<LugarDto> lugares = lugarService.findAll();
			List<LugarResponseDto> lugaresResponses = lugares.stream()
					.map(lugar -> new LugarResponseDto(
							lugar.getId(),
							new DireccionResponseDto(
									lugar.getDireccion().getId(),
									lugar.getDireccion().getCalle(),
									lugar.getDireccion().getAltura(),
									lugar.getDireccion().getProvinciaId(),
									lugar.getDireccion().getLocalidadId()),
							lugar.getHorarioApertura(),
							lugar.getHorarioCierre()))
					.toList();
			return ResponseEntity.ok(lugaresResponses);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al listar los lugares: " + e.getMessage());
		}
	}
	
	@GetMapping("/obtener/{id}")
	@Operation(
			summary = "Obtener un lugar por ID",
			description = "Obtiene los detalles de un lugar específico por su ID.  **Público**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lugar obtenido exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = LugarResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Lugar no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar no encontrado")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al obtener el lugar: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> obtenerLugarPorId(@PathVariable Long id) {
		try {
			LugarDto lugar = lugarService.findById(id);
			LugarResponseDto lugarResponse = new LugarResponseDto(
					lugar.getId(),
					new DireccionResponseDto(
							lugar.getDireccion().getId(),
							lugar.getDireccion().getCalle(),
							lugar.getDireccion().getAltura(),
							lugar.getDireccion().getProvinciaId(),
							lugar.getDireccion().getLocalidadId()),
					lugar.getHorarioApertura(),
					lugar.getHorarioCierre());
			return ResponseEntity.ok(lugarResponse);
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Lugar no encontrado");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al obtener el lugar: " + e.getMessage());
		}
	}
	
	@PutMapping("/actualizar/{id}")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Actualizar un lugar",
			description = "Actualiza los detalles de un lugar existente por su ID.  **Privado ADMIN**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lugar actualizado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar actualizado exitosamente")
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
			                          "path": "/api/lugares/actualizar/{id}",
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
			                          "path": "/api/lugares/actualizar/{id}",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404",
					description = "Lugar no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar no encontrado")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos del lugar",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos del lugar: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al actualizar el lugar: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> actualizarLugar(
			@PathVariable Long id,
			@Valid @RequestBody LugarRequestDto lugarDto, 
			BindingResult result) {
		
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos del lugar: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			LugarDto lugarNuevo = new LugarDto(
					null, // ID del lugar a actualizar
					new DireccionDto(
							null, // ID se generará automáticamente
							lugarDto.direccion().calle(),
							lugarDto.direccion().altura(),
							lugarDto.direccion().provinciaId(),
							lugarDto.direccion().localidadId()
					),
					lugarDto.horarioApertura(),
					lugarDto.horarioCierre());	 
			lugarService.update(id, lugarNuevo);
			return ResponseEntity.ok("Lugar actualizado exitosamente");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Lugar no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al actualizar el lugar: " + e.getMessage());
		}
		
	}
	
	@DeleteMapping("/eliminar/{id}")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Eliminar un lugar",
			description = "Elimina un lugar existente por su ID.  **Privado ADMIN**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lugar eliminado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar eliminado exitosamente")
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
			                          "path": "/api/lugares/eliminar/{id}",
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
			                          "path": "/api/lugares/eliminar/{id}",
			                          "user": "LucaLzt"
			                        }
			                        """
			                )
            		)
	        ),
			@ApiResponse(
					responseCode = "404",
					description = "Lugar no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Lugar no encontrado: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al eliminar el lugar: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> eliminarLugar(@PathVariable Long id) {
		try {
			lugarService.deleteById(id);
			return ResponseEntity.ok("Lugar eliminado exitosamente");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Lugar no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al eliminar el lugar: " + e.getMessage());
		}
	}
	
	@GetMapping("/buscarPorCalle")
	@Operation(
			summary = "Buscar lugares por nombre de calle",
			description = "Busca lugares que coincidan con el nombre de la calle proporcionada.  **Público**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lugares encontrados exitosamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = LugarResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error interno del servidor",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al buscar los lugares: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> buscarLugaresPorCalle(@RequestParam(required = false) String calle) {
		try {
			List<LugarDto> lugares;
			List<LugarResponseDto> lugaresResponses;
			if (calle != null && !calle.isEmpty()) {
				lugares = lugarService.findByCalle(calle);
				lugaresResponses = lugares.stream()
						.map(lugar -> new LugarResponseDto(
								lugar.getId(),
								new DireccionResponseDto(
										lugar.getDireccion().getId(),
										lugar.getDireccion().getCalle(),
										lugar.getDireccion().getAltura(),
										lugar.getDireccion().getProvinciaId(),
										lugar.getDireccion().getLocalidadId()),
								lugar.getHorarioApertura(),
								lugar.getHorarioCierre()))
						.toList();
			} else {
				lugares = lugarService.findAll();
				lugaresResponses = lugares.stream()
						.map(lugar -> new LugarResponseDto(
								lugar.getId(),
								new DireccionResponseDto(
										lugar.getDireccion().getId(),
										lugar.getDireccion().getCalle(),
										lugar.getDireccion().getAltura(),
										lugar.getDireccion().getProvinciaId(),
										lugar.getDireccion().getLocalidadId()),
								lugar.getHorarioApertura(),
								lugar.getHorarioCierre()))
						.toList();
			}
			return ResponseEntity.ok(lugaresResponses);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al buscar los lugares: " + e.getMessage());
		}
	}
	
}
