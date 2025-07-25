package com.oo2.grupo17.controllers.api;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.CambioPasswordDto;
import com.oo2.grupo17.dtos.ClienteDto;
import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.dtos.DireccionDto;
import com.oo2.grupo17.dtos.DisponibilidadDto;
import com.oo2.grupo17.dtos.LugarDto;
import com.oo2.grupo17.dtos.ProfesionalDto;
import com.oo2.grupo17.dtos.ServicioDto;
import com.oo2.grupo17.dtos.TurnoDto;
import com.oo2.grupo17.dtos.records.ClienteResponseDto;
import com.oo2.grupo17.dtos.records.ContactoRequestAuxDto;
import com.oo2.grupo17.dtos.records.ContactoResponseDto;
import com.oo2.grupo17.dtos.records.DireccionRequestDto;
import com.oo2.grupo17.dtos.records.DireccionResponseDto;
import com.oo2.grupo17.dtos.records.LugarResponseDto;
import com.oo2.grupo17.dtos.records.ServicioResponseDto;
import com.oo2.grupo17.dtos.records.TurnoRequestDto;
import com.oo2.grupo17.dtos.records.TurnoResponseDto;
import com.oo2.grupo17.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IContactoService;
import com.oo2.grupo17.services.IDireccionService;
import com.oo2.grupo17.services.IDisponibilidadService;
import com.oo2.grupo17.services.ILugarService;
import com.oo2.grupo17.services.IProfesionalService;
import com.oo2.grupo17.services.IServicioService;
import com.oo2.grupo17.services.ITurnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@RestController @Builder
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "basicAuth")
@PreAuthorize("hasRole('ROLE_CLIENTE')")
@Tag (name = "Cliente API", description = "API para la gestión de clientes")
public class ClienteRestController {

	private final ILugarService lugarService;
	private final ITurnoService turnoService;
	private final IClienteService clienteService;
	private final IContactoService contactoService;
	private final IServicioService servicioService;
	private final IDireccionService direccionService;
	private final IProfesionalService profesionalService;
	private final IDisponibilidadService disponibilidadService;
	
	/*
	 *  Se realizará la implementación de los métodos de la API REST para el cliente.
	 *  * Los métodos incluirán:
	 *  	- Ver Datos Cliente
	 *  	- Modificar contacto cliente
	 *  	- Modificar dirección cliente
	 *  	- Modificar contraseña cliente
	 *  	- Ver servicios disponibles
	 *  	- Ver lugares disponibles
	 *  	- Ver turnos disponibles
	 *  	- Solicitar turno
	 *  	- Reprogramar Turno
	 *  	- Cancelar turno
	 */
	
	@GetMapping("/verDatosCliente")
	@Operation(
			summary = "Ver datos del cliente",
			description = "Permite al cliente ver sus datos personales, incluyendo nombre, email, móvil y teléfono. " +
					"**Privado CLIENTE**"
	)
	public ResponseEntity<?> verDatosCliente(Principal principal) {
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		try {
			ClienteDto clienteDto = clienteService.findByEmail(email);
			
			ClienteResponseDto clienteResponse = new ClienteResponseDto(
					clienteDto.getId(),
					clienteDto.getNombre(),
					clienteDto.getDni(),
					clienteDto.getNroCliente(),
					new ContactoResponseDto(
							clienteDto.getContacto().getId(),
							clienteDto.getContacto().getEmail(),
							clienteDto.getContacto().getMovil(),
							clienteDto.getContacto().getTelefono(),
							clienteDto.getContacto().getDireccion() != null ?
								new DireccionResponseDto(
										clienteDto.getContacto().getDireccion().getId(),
										clienteDto.getContacto().getDireccion().getCalle(),
										clienteDto.getContacto().getDireccion().getAltura(),
										clienteDto.getContacto().getDireccion().getProvincia().getId(),
										clienteDto.getContacto().getDireccion().getLocalidad().getId()
								) : null // Si la dirección es null, no se incluye en la respuesta
					)
			);
			
			return ResponseEntity.ok(clienteResponse);
		} catch(EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Cliente no encontrado: " + e.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al obtener los datos del cliente: " + e.getMessage());
		}
		
	}
	
	@PostMapping("/modificarContacto")
	@Operation(
			summary = "Modificar contacto del cliente",
			description = "Permite al cliente modificar su información de contacto, incluyendo email, móvil y teléfono. " +
					"Si se modifica el email, se invalidará la sesión actual y se requerirá un nuevo inicio de sesión." +
					" **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Contacto modificado correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Contacto modificado correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos de contacto",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos de contacto: El email no es válido")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al modificar el contacto",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar el contacto: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> modificarContacto(Principal principal,
			@Valid @RequestBody ContactoRequestAuxDto contactoDto,
			BindingResult result,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de contacto: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
		
		try {
			ClienteDto clienteDto = clienteService.findByEmail(email);
			ContactoDto contactoNuevo = new ContactoDto(
					clienteDto.getId(),
					contactoDto.email(),
					contactoDto.movil(),
					contactoDto.telefono(),
					null // Dirección no se modifica aquí
			);	 
			
			clienteService.updatearContactoUserEntity(contactoNuevo);
			String emailNuevo = contactoNuevo.getEmail();
			if(!email.equals(emailNuevo)) {
				// Invalidar la sesión
	            request.getSession().invalidate();
	            SecurityContextHolder.clearContext();
				return ResponseEntity.ok("Contacto modificado correctamente. Se ha cambiado el email a: " + emailNuevo);
			}
			return ResponseEntity.ok("Contacto modificado correctamente.");
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al modificar el contacto: " + e.getMessage());
		}
		
	}
	
	@PostMapping("/modificarDireccion")
	@Operation(
			summary = "Modificar dirección del cliente",
			description = "Permite al cliente modificar su dirección de contacto. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Dirección modificada correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Dirección modificada/creada correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos de contacto",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos de contacto: La dirección es obligatoria")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al modificar la dirección",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar la dirección: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> modificarDireccion(Principal principal,
			@Valid @RequestBody DireccionRequestDto direccionDto,
			BindingResult result,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de contacto: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
		
		try {
			
			ContactoDto contactoDto = contactoService.findByEmail(email);
			
			DireccionDto direccionNueva = new DireccionDto(
					// Si la dirección ya existe, se usa su ID, si no, se asigna null para crear una nueva
					contactoDto.getDireccion().getId() != null ? contactoDto.getDireccion().getId() : null, 
					direccionDto.calle(),
					direccionDto.altura(),
					direccionDto.provinciaId(),
					direccionDto.localidadId()
			);
			ContactoDto contactoActual = contactoService.findByEmail(email);
			if(contactoActual.getDireccion() == null) {
				direccionService.crearDireccion(contactoActual, direccionNueva);
				return ResponseEntity.ok("Dirección creada correctamente.");
			} else {
				direccionService.actualizarDireccion(contactoActual, direccionNueva);
				return ResponseEntity.ok("Dirección modificada correctamente.");
			}
			
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al modificar la dirección: " + e.getMessage());
		}
	}
	
	@PostMapping("/modificarContrasena")
	@Operation(
			summary = "Modificar contraseña del cliente",
			description = "Permite al cliente modificar su contraseña. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Contraseña modificada correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Contraseña modificada correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "422",
					description = "Error en los datos de la contraseña",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error en los datos: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al modificar la contraseña",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al modificar la contraseña: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> modificarContrasena(Principal principal,
			@Valid @RequestBody CambioPasswordDto cambioPasswordDto,
			BindingResult result,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
		
		try {
			ClienteDto clienteDto = clienteService.findByEmail(email);
			clienteService.cambiarContrasena(clienteDto, cambioPasswordDto);
			return ResponseEntity.ok("Contraseña modificada correctamente.");
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al modificar la contraseña: " + e.getMessage());
		}
	}
	
	@GetMapping("/verServiciosDisponibles")
	@Operation(
			summary = "Ver servicios disponibles",
			description = "Permite al cliente ver los servicios disponibles. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Servicios disponibles obtenidos correctamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ServicioResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "No hay servicios disponibles en este momento",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "No hay servicios disponibles en este momento.")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al obtener los servicios disponibles",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al obtener los servicios disponibles: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<?> verServiciosDisponibles() {
		try {
			List<ServicioDto> servicios = servicioService.findAll();
			
			if(servicios.isEmpty()) {
				return ResponseEntity.status(400).body("No hay servicios disponibles en este momento.");
			}
			List<ServicioResponseDto> serviciosResponse = servicios.stream()
					.map(servicio -> new ServicioResponseDto(
							servicio.getId(),
							servicio.getNombre(),
							servicio.getDescripcion(),
							servicio.getPrecio(),
							servicio.getLugaresIds()))
					.toList();
			return ResponseEntity.ok(serviciosResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al obtener los servicios disponibles: " + e.getMessage());
		}
	}
	
	@GetMapping("/verLugaresDisponibles")
	@Operation(
			summary = "Ver lugares disponibles",
			description = "Permite al cliente ver los lugares disponibles. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Lugares disponibles obtenidos correctamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = LugarResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "No hay lugares disponibles en este momento",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "No hay lugares disponibles en este momento.")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al obtener los lugares disponibles",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al obtener los lugares disponibles: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<?> verLugaresDisponibles() {
		try {
			List<LugarDto> lugares = lugarService.findAll();
			if(lugares.isEmpty()) {
				return ResponseEntity.status(400).body("No hay lugares disponibles en este momento.");
			}
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
			return ResponseEntity.status(500).body("Error al obtener los lugares disponibles: " + e.getMessage());
		}
	}
	
	@GetMapping("/verTurnosDisponibles")
	@Operation(
			summary = "Ver turnos disponibles",
			description = "Permite al cliente ver los turnos disponibles. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Turnos disponibles obtenidos correctamente",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = TurnoResponseDto.class)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "No hay turnos disponibles en este momento",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "No hay turnos disponibles en este momento.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Cliente no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Cliente no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al obtener los turnos disponibles",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al obtener los turnos disponibles: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<?> verTurnosDisponibles(Principal principal) {
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        try {
        	
        	// Busco al cliente por email y obtengo sus turnos disponibles
        	ClienteDto cliente = clienteService.findByEmail(email);
        	List<TurnoDto> turnosDisponibles = turnoService.buscarTurnosPorClienteId(cliente.getId());
        	
        	if(turnosDisponibles.isEmpty()) {
        		return ResponseEntity.status(400).body("No hay turnos disponibles en este momento.");
			} else {
				// Mapear los turnos a un formato adecuado para la respuesta
				List<TurnoResponseDto> turnosResponse = turnosDisponibles.stream()
						.map(turno -> new TurnoResponseDto(
								turno.getId(),
								turno.getCliente().getNombre(),
								turno.getProfesional().getNombre(),
								turno.getLugar().getDireccion().getCalle() + " " + turno.getLugar().getDireccion().getAltura(),
								turno.getServicio().getNombre(),
								turno.getDisponibilidad().getInicio()
								)
						)
						.toList();
				return ResponseEntity.ok(turnosResponse);
        	}
        	
        } catch (EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Cliente no encontrado: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al obtener los turnos disponibles: " + e.getMessage());
		}
        
	}
	
	@PostMapping("/solicitarTurno")
	@Operation(
			summary = "Solicitar turno",
			description = "Permite al cliente solicitar un turno para un servicio específico. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Turno solicitado exitosamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Turno solicitado exitosamente.")
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Datos del turno inválidos o disponibilidad ocupada",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Datos del turno inválidos o la disponibilidad seleccionada ya está ocupada.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Cliente/Servicio/Lugar/Profesional no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Cliente/Servicio/Lugar/Profesional no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al solicitar el turno",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al solicitar el turno: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> solicitarTurno(
			@Valid @RequestBody TurnoRequestDto turnoDto,
			BindingResult result,
			Principal principal) {
	
		if(result.hasErrors()) {
			return ResponseEntity.badRequest().body("Datos del turno inválidos.");
		}
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		try {
			// Buscar al cliente por email
			ClienteDto cliente = clienteService.findByEmail(email);
			
			// Buscar Profesional, Lugar y Servicio por nombre
			// Buscar Disponibilidad por Profesional y fecha
			ProfesionalDto profesional = profesionalService.findByNombre(turnoDto.profesional());
			LugarDto lugar = lugarService.findByCalleAndAltura(turnoDto.direccion(), turnoDto.altura());
			ServicioDto servicio = servicioService.findByNombre(turnoDto.servicio());
			DisponibilidadDto disponibilidad = disponibilidadService.findByProfesionalAndInicio(
					profesional.getId(), turnoDto.disponibilidad());
			
			// Crear el Turno
			TurnoDto turnoNuevo = new TurnoDto(
					null, // ID se asigna automáticamente
					cliente,
					profesional,
					lugar,
					servicio,
					disponibilidad
			);
			
			// Validar que la disponibilidad no esté ocupada
			if(disponibilidad.isOcupado()) {
				return ResponseEntity.status(400).body("La disponibilidad seleccionada ya está ocupada.");
			}
			
			turnoService.crearTurno(turnoNuevo);
			
			return ResponseEntity.ok("Turno solicitado exitosamente.");
			
		} catch(EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Cliente/Servicio/Lugar/Profesional no encontrado: " + e.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al solicitar el turno: " + e.getMessage());
		}
		
	}
	
	@PostMapping("/reprogramarTurno")
	@Operation(
			summary = "Reprogramar turno",
			description = "Permite al cliente reprogramar un turno previamente reservado. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Turno reprogramado correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Turno reprogramado correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Datos de reprogramación inválidos o disponibilidad ocupada",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Datos de reprogramación inválidos o la disponibilidad seleccionada ya está ocupada.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Cliente/Servicio/Lugar/Profesional no encontrado o cliente no autorizado para reprogramar el turno",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Cliente/Servicio/Lugar/Profesional no encontrado: {mensaje de error}\n" +
									"Cliente no autorizado para reprogramar este turno.")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al reprogramar el turno",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al reprogramar el turno: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> reprogramarTurno(
			@RequestParam @NotNull @Positive Long turnoId,
			@RequestParam @NotNull @Positive Long disponibilidadId,
			Principal principal) {
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		try {
			// Buscar al cliente por email
			ClienteDto cliente = clienteService.findByEmail(email);
			
			// Validar que el turnoId perteneza al cliente autorizado
			if(!clienteService.tieneTurno(turnoId, cliente.getId())) {
				return ResponseEntity.status(404).body("Cliente no autorizado para reprogramar este turno.");
			}
			
			// Reprogramar el turno
			if(turnoService.reprogramarTurno(turnoId, disponibilidadId)) {
				return ResponseEntity.ok("Turno reprogramado correctamente.");
			} else {
				return ResponseEntity.status(400).body("No se pudo reprogramar el turno.");
			}
			
		} catch(EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Cliente/Servicio/Lugar/Profesional no encontrado: " + e.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al reprogramar el turno: " + e.getMessage());
		}
		
	}
	
	@PostMapping("/cancelarTurno")
	@Operation(
			summary = "Cancelar turno",
			description = "Permite al cliente cancelar un turno previamente reservado. **Privado CLIENTE**"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Turno cancelado correctamente",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Turno cancelado correctamente.")
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "ID del turno inválido o no encontrado",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "El ID del turno es inválido.")
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Turno no encontrado o cliente no autorizado para cancelar el turno",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Cliente/Turno no encontrado: {mensaje de error}")
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Error al cancelar el turno",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(type = "string", example = "Error al cancelar el turno: {mensaje de error}")
					)
			)
	})
	public ResponseEntity<String> cancelarTurno(
			@RequestBody Long turnoId,
			Principal principal) {
		
		// Validar que el turnoId no sea nulo o negativo
		if(turnoId == null || turnoId <= 0) {
			return ResponseEntity.badRequest().body("El ID del turno es inválido.");
		}
		
		// Obtener el email del usuario autenticado
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
		
		try {
			// Verificar si el cliente tiene permiso para cancelar el turno
			ClienteDto cliente = clienteService.findByEmail(email);
			// Cancelar el turno
			if(clienteService.cancelarTurno(turnoId, cliente.getId())) {
				// Si se cancela correctamente, retornar mensaje de éxito
				return ResponseEntity.ok("Turno cancelado correctamente.");
			} else {
				// Si no se encuentra el turno o el cliente no tiene permiso, retornar error
				return ResponseEntity.status(400).body("Cliente no autorizado.");
			}
		} catch(EntidadNoEncontradaException e) {
			return ResponseEntity.status(404).body("Cliente/Turno no encontrado: " + e.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(500).body("Error al cancelar el turno: " + e.getMessage());
		}
		
	}
	
}
