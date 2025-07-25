package com.oo2.grupo17.controllers.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.dtos.records.ContactoRequestAuxDto;
import com.oo2.grupo17.dtos.records.ContactoResponseDto;
import com.oo2.grupo17.dtos.records.DireccionRequestDto;
import com.oo2.grupo17.dtos.records.DireccionResponseDto;
import com.oo2.grupo17.dtos.records.DisponibilidadResponseDto;
import com.oo2.grupo17.dtos.records.ProfesionalResponseDto;
import com.oo2.grupo17.dtos.records.ServicioResponseDto;
import com.oo2.grupo17.dtos.records.TurnoResponseDto;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IDisponibilidadService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Builder;

@RestController() @Builder
@RequestMapping("/api/profesional")
@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
@Tag(name = "Profesional", description = "Operaciones relacionadas con los profesionales")
public class ProfesionalRestController {
	
	
	/* Traer Turnos del Profesional	X
	 * Traer Servicios Habilitados del Profesional	X
	 * Traer Disponibilidades posteriores a la fecha actual X
	 * Ver Datos del profesional X
	 * Modificar Contacto X
	 * Modificar Direccion X
	 * Modificar Contraseña X
	 * Cancelar Turno ID
	 */

	private IProfesionalService profesionalService;
	private ITurnoService turnoService;
	private IServicioService servicioService;
	private IDisponibilidadService disponibilidadService;
	private IContactoService contactoService;
	private IDireccionService direccionService;
	
	@GetMapping("/turnos")
	@Operation(
			summary = "Traer Turnos", 
			description = "Se todos los Turnos del Profesional. **Privado PROFESIONAL**")
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
					responseCode = "500",
					description = "Problemas en el sistema",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> traerTurnos() {
		try{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			List<TurnoDto> turnos = turnoService.buscarTurnosPorProfesionalId(profesionalDto.getId());
			List<TurnoResponseDto> turnoResponse = turnos.stream()
					.map(turno -> new TurnoResponseDto(
							turno.getId(),
							turno.getCliente().getNombre(),
							turno.getProfesional().getNombre(),
							turno.getLugar().getDireccion().getCalle(),
							turno.getServicio().getNombre(),
							turno.getDisponibilidad().getInicio()))
					.toList();
			if(turnoResponse.isEmpty()) {
				return ResponseEntity.status(200).body("El Profesional no tiene Turnos asignados.");
			}
			return ResponseEntity.status(200).body(turnoResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/servicios")
	@Operation(
			summary = "Traer Servicios Habilitados", 
			description = "Se traen los Servicios habilitados del Profesional. **Privado PROFESIONAL**")
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
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> traerServiciosHabilitados() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			List<ServicioDto> servicios = profesionalDto.getServiciosIds().stream()
					.map(servicioService::findById)
					.toList();
			List<ServicioResponseDto> servicioResponse = servicios.stream()
					.map(servicio -> new ServicioResponseDto(
							servicio.getId(),
							servicio.getNombre(),
							servicio.getDescripcion(),
							servicio.getPrecio(),
							servicio.getLugaresIds()))
					.toList();
			if(servicioResponse.isEmpty()) {
				return ResponseEntity.status(200).body("El Profesional no tiene Servicios habilitados.");
			}
			return ResponseEntity.status(200).body(servicioResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/disponibilidades")
	@Operation(
			summary = "Traer Disponibilidades Posteriores", 
			description = "Se traen las Disponibilidades posteriores a la fecha actual del Profesional. **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Disponibilidades encontradas.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = DisponibilidadResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> traerDisponibilidadesPosteriores() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			List<DisponibilidadDto> disponibilidades = disponibilidadService.obtenerDisponibilidadesPorProfesional(profesionalDto.getId());
			List<DisponibilidadResponseDto> dispResponse = disponibilidades.stream()
					.map(disponibilidad -> new DisponibilidadResponseDto(
							disponibilidad.getId(),
							disponibilidad.getProfesional().getNombre(),
							disponibilidad.getInicio(),
							disponibilidad.getDuracion(),
							disponibilidad.isOcupado()))
					.toList();
			if(dispResponse.isEmpty()) {
				return ResponseEntity.status(200).body("El Profesional no tiene Disponibilidades.");
			}
			return ResponseEntity.status(200).body(dispResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/verDatosProfesional")
	@Operation(summary = "Ver Datos del Profesional", description = "Se traen los datos del Profesional autenticado. **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Datos del Profesional encontrados.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ProfesionalResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<?> verDatosProfesional() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			ProfesionalResponseDto profResponse = new ProfesionalResponseDto(
					profesionalDto.getId(),
					profesionalDto.getNombre(),
					profesionalDto.getDni(),
					profesionalDto.getMatricula(),
					new ContactoResponseDto(
							profesionalDto.getContacto().getId(),
							profesionalDto.getContacto().getEmail(),
							profesionalDto.getContacto().getMovil(),
							profesionalDto.getContacto().getTelefono(),
							profesionalDto.getContacto().getDireccion() != null ?
							new DireccionResponseDto(
									profesionalDto.getContacto().getDireccion().getId(),
									profesionalDto.getContacto().getDireccion().getCalle(),
									profesionalDto.getContacto().getDireccion().getAltura(),
									profesionalDto.getContacto().getDireccion().getProvincia().getId(),
									profesionalDto.getContacto().getDireccion().getLocalidad().getId()
							) : null
					)
			);
			return ResponseEntity.status(200).body(profResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	
	@PutMapping("/modificarContacto")
	@Operation(
			summary = "Modificar Contacto", 
			description = "Permite al profesional modificar su información de contacto, incluyendo email, móvil y teléfono. " +
							"Si se modifica el email, se invalidará la sesión actual y se requerirá un nuevo inicio de sesión." +
							" **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Contacto modificado correctamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Contacto modificado correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos ingresados.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> modificarContacto(@Valid @RequestBody ContactoRequestAuxDto contactoRequest, BindingResult results,
			HttpServletRequest request) {
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			ContactoDto contactoDto = new ContactoDto(
					profesionalDto.getContacto().getId(),
					contactoRequest.email(),
					contactoRequest.movil(),
					contactoRequest.telefono(),
					null
					);
			profesionalService.updatearContactoUserEntity(contactoDto);
			String emailNuevo = contactoDto.getEmail();
			if(!email.equals(emailNuevo)) {
				request.getSession().invalidate();
				SecurityContextHolder.clearContext();
				return ResponseEntity.status(200).body("Contacto modificado correctamente. Se ha cambiado el email a: " + emailNuevo);
			} else {
				return ResponseEntity.status(200).body("Contacto modificado correctamente.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@PostMapping("/modificarDireccion")
	@Operation(
			summary = "Modificar Dirección", 
			description = "Permite al profesional modificar su dirección. Si no tiene una dirección, se creará una nueva. **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Dirección modificada correctamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Dirección modificada correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos ingresados.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> modificarDireccion(@Valid @RequestBody DireccionRequestDto direccionRequestDto, BindingResult results) {
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();

			ContactoDto contactoDto = contactoService.findByEmail(email);
			DireccionDto direccionDto = new DireccionDto(
					contactoDto.getDireccion() != null ? contactoDto.getDireccion().getId() : null,
					direccionRequestDto.calle(),
					direccionRequestDto.altura(),
					direccionRequestDto.provinciaId(),
					direccionRequestDto.localidadId());
			if(contactoDto.getDireccion() == null) {
				direccionService.crearDireccion(contactoDto, direccionDto);
			} else {
				direccionService.actualizarDireccion(contactoDto, direccionDto);
			}			
			return ResponseEntity.status(200).body("Dirección modificada correctamente.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@PutMapping("/modificarContraseña")
	@Operation(
			summary = "Modificar Contraseña", 
			description = "Permite al profesional modificar su contraseña. **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Contraseña modificada correctamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Contraseña modificada correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos ingresados.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> modificarContraseña(@Valid @RequestBody CambioPasswordDto nuevaContraseña, BindingResult results) {
		if(results.hasErrors()) {
			String errors = results.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; \n"));
			return ResponseEntity.status(422).body("Error en los datos ingresados: " + errors);
		}
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			profesionalService.cambiarContrasena(profesionalDto, nuevaContraseña);
			return ResponseEntity.status(200).body("Contraseña modificada correctamente.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/cancelarTurno/{id}")
	@Operation(
			summary = "Cancelar Turno", 
			description = "Permite al profesional cancelar un turno por ID. **Privado PROFESIONAL**")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Turno cancelado correctamente.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Turno cancelado correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Turno no encontrado.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al cancelar turno: {errorMessage}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Problemas en el Sistema.",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {errorMessage}")
					)
			)
	})
	public ResponseEntity<String> cancelarTurno(@PathVariable("id") Long turnoId) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			
			ProfesionalDto profesionalDto = profesionalService.findByEmail(email);
			profesionalService.cancelarTurno(profesionalDto.getId(), turnoId);
			
			return ResponseEntity.status(200).body("Turno cancelado correctamente.");
		} catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Error al cancelar turno: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
}
