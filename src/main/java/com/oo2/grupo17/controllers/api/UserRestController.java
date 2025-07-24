package com.oo2.grupo17.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.ProfesionalRegistradoDto;
import com.oo2.grupo17.dtos.records.LoginDto;
import com.oo2.grupo17.dtos.records.RegisterClienteDto;
import com.oo2.grupo17.dtos.records.RegisterProfesionalDto;
import com.oo2.grupo17.exceptions.EntidadDuplicadaException;
import com.oo2.grupo17.services.IClienteService;
import com.oo2.grupo17.services.IProfesionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;

@RestController @Builder
@RequestMapping("auth/users")
@Tag(name = "User API", description = "API para gestionar usuarios del sistema")
public class UserRestController {
	
	private final IClienteService clienteService;
	private final IProfesionalService profesionalService;
	private final AuthenticationManager authManager;
	
	@PostMapping("/registrarCliente")
	@Operation(
			summary = "Registro de un nuevo cliente",
			description = "Permite registrar un nuevo cliente en el sistema"
	)
	@ApiResponses(value = {
	    @ApiResponse(
	    		responseCode = "201", 
	    		description = "Registro exitoso",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Cliente registrado exitosamente")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "409", 
	    		description = "El cliente ya existe",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "El cliente ya existe: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "422", 
	    		description = "Error en los datos de registro",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error en los datos de registro: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "500",
	    		description = "Error interno del servidor",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error al registrar el cliente: {mensaje de error}")
	    		)
	    )
	})
	public ResponseEntity<String> registrarCliente(@Valid @RequestBody RegisterClienteDto registerDto,
			BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de registro: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			ClienteRegistroDto cliente = new ClienteRegistroDto(
					registerDto.email(),
					registerDto.password(),
					registerDto.nombre(),
					registerDto.dni(),
					registerDto.movil(),
					registerDto.telefono()
					);
			clienteService.registrarCliente(cliente);
			return ResponseEntity.status(201).body("Cliente registrado exitosamente");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("El cliente ya existe: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al registrar el cliente: " + e.getMessage());
		}
	}
	
	@PostMapping("/registrarProfesional")
	@Operation(
		summary = "Registro de un nuevo profesional", 
		description = "Permite registrar un nuevo profesional en el sistema." +
		"**La contraseña se enviará automáticamente al email proporcionado.**"
	)	
	@ApiResponses(value = {
	    @ApiResponse(
	    		responseCode = "201", 
	    		description = "Registro exitoso",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Profesional registrado exitosamente")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "409", 
	    		description = "El profesional ya existe",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "El profesional ya existe: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "422",
	    		description = "Error en los datos de registro",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error en los datos de registro: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "500",
	    		description = "Error interno del servidor",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error al registrar el profesional: {mensaje de error}")
	    		)
	    )
	})
	public ResponseEntity<String> registrarProfesional(@Valid @RequestBody RegisterProfesionalDto registerDto,
			BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de registro: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			ProfesionalRegistradoDto profesional = new ProfesionalRegistradoDto(
					registerDto.email(),
					registerDto.nombre(),
					registerDto.dni(),
					registerDto.matricula(),
					registerDto.movil(),
					registerDto.telefono()
					);
			profesionalService.registrarProfesional(profesional);
			return ResponseEntity.status(201).body("Profesional registrado exitosamente");
		} catch (EntidadDuplicadaException e) {
			return ResponseEntity.status(409).body("El profesional ya existe: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al registrar el profesional: " + e.getMessage());
		}
	}
	
	@PostMapping("/login")
	@Operation(
			summary = "Inicio de sesión de un usuario",
			description = "Permite a un usuario iniciar sesión en el sistema"
	)
	@ApiResponses(value = {
	    @ApiResponse(
	    		responseCode = "200",
	    		description = "Inicio de sesión exitoso",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Inicio de sesión exitoso para: {nombre de usuario}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "401",
	    		description = "Credenciales inválidas",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Credenciales inválidas: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "422",
	    		description = "Error en los datos de inicio de sesión",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error en los datos de inicio de sesión: {mensaje de error}")
	    		)
	    ),
	    @ApiResponse(
	    		responseCode = "500",
	    		description = "Error interno del servidor",
	    		content = @Content(
	    				mediaType = "text/plain",
	    				schema = @Schema(type = "string", example = "Error al iniciar sesión: {mensaje de error}")
	    		)
	    )
	})
	public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto, 
			BindingResult result) {
		if(result.hasErrors()) {
			return ResponseEntity.status(422).body("Error en los datos de inicio de sesión: " 
					+ result.getAllErrors().get(0).getDefaultMessage());
		}
		try {
			Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginDto.username(),
					loginDto.password()
				)
			);
			return ResponseEntity.status(200).body("Inicio de sesión exitoso para: " + authentication.getName());
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Credenciales inválidas: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al iniciar sesión: " + e.getMessage());
		} 
		
	}
	
}
