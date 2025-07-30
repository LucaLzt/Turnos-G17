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
import com.oo2.grupo17.dtos.records.ServicioRequestAuxDto;
import com.oo2.grupo17.dtos.records.ServicioRequestDto;
import com.oo2.grupo17.dtos.records.ServicioResponseDto;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

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
@RequestMapping("api/servicio")
@Tag(name = "Servicio API", description = "API para gestionar servicios")
public class ServicioRestController {
	
	private final IServicioService servicioService;
	private final ITurnoService turnoService;
	
	@GetMapping("/{id}/traer")
	@Operation(
			summary = "Traer servicio", 
			description = "Se trae un Servicio por ID. **Público**")
	@ApiResponses( value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Servicio encontrado.",
					content = @Content( 
							mediaType = "application/json",
							schema = @Schema(implementation = ServicioResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "404", 
					description = "Servicio no encontrado.",
					content = @Content( 
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al traer el Servicio: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Probremas en el sistema.",
					content = @Content( 
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> getServicio(@PathVariable Long id){
		try {
		ServicioDto dto = servicioService.findById(id);
		ServicioResponseDto responseDto = new ServicioResponseDto(dto.getId(), 
				dto.getNombre(), 
				dto.getDescripcion(), 
				dto.getPrecio(), 
				dto.getLugaresIds());
		return ResponseEntity.ok(responseDto);
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al traer el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/traer")
	@Operation(
			summary = "Traer todos los Servicios", 
			description = "Se traen todos los servicios. **Público**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Servicios encontrados.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ServicioResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> traerServicios() {
		try {
			List<ServicioDto> dtos = servicioService.findAll();
			List<ServicioResponseDto> responseDtos = new ArrayList<>();
			for(ServicioDto dto : dtos) {
				responseDtos.add(new ServicioResponseDto(
						dto.getId(),
						dto.getNombre(),
						dto.getDescripcion(),
						dto.getPrecio(),
						dto.getLugaresIds()
						));
			}
			return ResponseEntity.status(200).body(responseDtos);
		} catch (Exception e){
			return ResponseEntity.status(500).body("Error en el Sistema: " + e.getMessage());
		}
	}
	
	@PostMapping("/agregar")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Agregar Servicio", 
			description = "Se agrega un Servicio. **Privado ADMIN**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Servicio agregado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Servicio agregado correctamente.")
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
					responseCode = "422", 
					description = "Datos inválidos.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "409", 
					description = "Servicio ya creado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al agregar el Servicio: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> agregarServicio(@Valid @RequestBody ServicioRequestDto requestDto, BindingResult results){
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
		
		try {
			ServicioDto dto = new ServicioDto(
					null, // ID se asigna automáticamente
					requestDto.nombre(), 
					requestDto.descripcion(), 
					requestDto.precio(), 
					new ArrayList<>());
			servicioService.save(dto);
			return ResponseEntity.status(200).body("Servicio agregado correctamente.");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("Error al agregar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema: " + e.getMessage());
		}
		
	}
	
	@DeleteMapping("/{id}/eliminar")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Eliminar Servicio", 
			description = "Se elimina un Servicio por ID. **Privado ADMIN**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Servicio eliminado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Servicio eliminado correctamente.")
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
					responseCode = "404", 
					description = "Servicio no encontrado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al eliminar el Servicio: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> eliminarServicio(@PathVariable Long id){
		try {
			boolean tieneTurnos = turnoService.existsByServicio(id);
			servicioService.deleteById(id, tieneTurnos);
			return ResponseEntity.status(200).body("Servicio eliminado correctamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al eliminar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema: " + e.getMessage());
		}

	}
	
	@PutMapping("/{id}/modificar")
	@SecurityRequirement(name = "basicAuth")
	@Operation(
			summary = "Modificar Servicio", 
			description = "Se modifica un Servicio por ID. **Privado ADMIN**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Servicio modificado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Servicio modificado correctamente.")
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
					responseCode = "404", 
					description = "Servicio no encontrado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar el Servicio: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "409", 
					description = "Servicio ya creado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar el Servicio: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "422", 
					description = "Datos inválidos.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500", 
					description = "Problemas en el sistema",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> modificarServicio(@PathVariable Long id, @Valid @RequestBody ServicioRequestAuxDto requestDto, BindingResult results){
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
				try {
			ServicioDto dto = new ServicioDto(
					null, // ID se asigna automáticamente
					requestDto.nombre(), 
					requestDto.descripcion(), 
					requestDto.precio(), 
					requestDto.idsLugares());
			servicioService.update(id, dto);
			return ResponseEntity.status(200).body("Servicio modificado correctamente.");
		} catch (EntidadNoEncontradaException e){
			return ResponseEntity.status(404).body("Error al modificar el Servicio: " + e.getMessage());
		} catch (EntidadDuplicadaException e){
			return ResponseEntity.status(409).body("Error al modificar el Servicio: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error en el Sistema: " + e.getMessage());
		}
	}
}
