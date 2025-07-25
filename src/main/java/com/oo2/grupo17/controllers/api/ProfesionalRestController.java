package com.oo2.grupo17.controllers.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.dtos.records.ContactoRequestAuxDto;
import com.oo2.grupo17.dtos.records.ContactoResponseDto;
import com.oo2.grupo17.dtos.records.DireccionResponseDto;
import com.oo2.grupo17.dtos.records.DisponibilidadResponseDto;
import com.oo2.grupo17.dtos.records.ProfesionalResponseDto;
import com.oo2.grupo17.dtos.records.ServicioResponseDto;
import com.oo2.grupo17.dtos.records.TurnoResponseDto;
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
	 * Traer Disponibilidades posteriores a la fecha actual ?
	 * Ver Datos del profesional X
	 * Modificar Contacto
	 * Modificar Direccion
	 * Cancelar Turno ID
	 */

	private IProfesionalService profesionalService;
	private ITurnoService turnoService;
	private IServicioService servicioService;
	private IDisponibilidadService disponibilidadService;
	
	@GetMapping("/turnos")
	@Operation(summary = "Traer Turnos", description = "Se todos los Turnos del Profesional. **Privado PROFESIONAL**")
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
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {error_message}")
							)
			)
	})
	public ResponseEntity<?> traerTurnos(){
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
			return ResponseEntity.status(200).body(turnoResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	
	@GetMapping("/servicios")
	@Operation(summary = "Traer Servicios Habilitados", description = "Se traen los Servicios habilitados del Profesional. **Privado PROFESIONAL**")
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
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {error_message}")
							)
			)
	})
	public ResponseEntity<?> traerServiciosHabilitados(){
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
			return ResponseEntity.status(200).body(servicioResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Problemas en el Sistema: " + e.getMessage());
		}
	}
	/// REVISAR
	@GetMapping("/disponibilidades")
	public ResponseEntity<?> traerDisponibilidadesPosteriores(){
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
							schema = @Schema(type = "string", example = "Problemas en el Sistema: {error_message}")
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
	
	
	@PostMapping("/modificarContacto")
	@Operation(summary = "Modificar Contacto", description = "Permite al profesional modificar su información de contacto, incluyendo email, móvil y teléfono. " +
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
							schema = @Schema(type = "string", example = "Error en los datos ingresados: {error_message}")
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
	public ResponseEntity<String> modificarContacto(@Valid @RequestBody ContactoRequestAuxDto contactoRequest, BindingResult results,
			HttpServletRequest request){
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
}
