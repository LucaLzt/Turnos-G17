package com.oo2.grupo17.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestGlobalExceptionHandler {
	
	// Errores de Autenticación (401)
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
		Map<String, Object> errorResponse = createErrorResponse(
				"Unauthorized",
				"Error de autenticación: " + ex.getMessage(),
				401
		);
		return ResponseEntity.status(401).body(errorResponse);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
		Map<String, Object> errorResponse = createErrorResponse(
				"Unauthorized",
				"Credenciales inválidas. Verifica tu usuario y contraseña.",
				401
		);
		return ResponseEntity.status(401).body(errorResponse);
	}
	
	// Errores de Autorización (403)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
		Map<String, Object> errorResponse = createErrorResponse(
				"Forbidden",
				"Acceso denegado: " + ex.getMessage(),
				403
		);
		return ResponseEntity.status(403).body(errorResponse);
	}
	
	// Errores de Validación (422)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		String errors = result.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
		
		Map<String, Object> errorResponse = createErrorResponse(
				"Validation Error",
				"Errores de validación: " + errors,
				422
		);
		return ResponseEntity.status(422).body(errorResponse);
	}
	
	// Error General (500)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
		Map<String, Object> errorResponse = createErrorResponse(
				"Internal Server Error",
				"Error interno del servidor: " + ex.getMessage(),
				500
		);
		return ResponseEntity.status(500).body(errorResponse);
	}
	
	// Auxiliares
	private Map<String, Object> createErrorResponse(String error, String message, int status) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", error);
		errorResponse.put("message", message);
		errorResponse.put("status", status);
		errorResponse.put("timestamp", Instant.now().toString());
		errorResponse.put("user", getCurrentUser());
		return errorResponse;
	}
	
	private String getCurrentUser() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return auth != null ? auth.getName() : "anonymous";
		} catch (Exception e) {
			return "unknown";
		} 
	}
	
}
