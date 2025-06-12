package com.oo2.grupo17.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo17.dtos.ClienteRegistroDto;
import com.oo2.grupo17.dtos.records.RegisterDto;
import com.oo2.grupo17.services.IClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Builder;

@Builder
@RestController
@RequestMapping("auth/users")
public class UserController {
	
	private final IClienteService clienteService;
	
	@PostMapping("/register")
	@Operation(summary = "Registro de un nuevo usuario", description = "Permite registrar un nuevo usuario en el sistema")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Registro exitoso"),
	    @ApiResponse(responseCode = "401", description = "Credenciales no válidas")
	})
	public ResponseEntity<String> registerCliente(@org.springframework.web.bind.annotation.RequestBody RegisterDto registerDto) {
		ClienteRegistroDto cliente = new ClienteRegistroDto(
				registerDto.email(),
				registerDto.password(),
				registerDto.nombre(),
				registerDto.dni(),
				registerDto.movil(),
				registerDto.telefono()
				);
		clienteService.registrarCliente(cliente);
		return ResponseEntity.status(201).body("Usuario registrado exitosamente");
	}
	
}
