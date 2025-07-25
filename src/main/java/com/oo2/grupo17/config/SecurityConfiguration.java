package com.oo2.grupo17.config;

import com.oo2.grupo17.services.implementation.UserService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                	// Públicos
                    auth.requestMatchers("/css/**", "/js/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/auth/**").permitAll();
                    
                    // API Públicas
                    auth.requestMatchers(
                    		"/api/lugares/obtenerTodos",
                    		"/api/lugares/obtener/{id}",
                    		"/api/lugares/obtenerLugarPorId",
                    		"/api/lugares/buscarPorCalle"
                    ).permitAll();
                    auth.requestMatchers(
                    		"/api/especialidades/obtenerTodos",
                    		"/api/especialidades/obtener/{id}"
                    ).permitAll();
                    
                    // API Privadas
                    auth.requestMatchers("/api/profesional/**").hasRole("PROFESIONAL");
                    auth.requestMatchers("/api/cliente/**").hasRole("CLIENTE");
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/lugares/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/especialidades/**").hasRole("ADMIN");
                    
                    auth.anyRequest().authenticated();
                })
                .httpBasic(httpBasic -> {
                	httpBasic.authenticationEntryPoint(customAuthenticationEntryPoint());
                })
                .exceptionHandling(exceptions -> exceptions
                		.authenticationEntryPoint(customAuthenticationEntryPoint()) // 401
                		.accessDeniedHandler(customAccessDeniedHandler()) // 403
				)
                .formLogin(login -> {
                    login.loginPage("/auth/login");
                    login.loginProcessingUrl("/auth/loginProcess");
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    login.defaultSuccessUrl("/auth/loginSuccess", true);
                    login.permitAll();
                })
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login?logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                )
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    
    // Manejador de excepción 401 (No autenticado)
    @Bean
    AuthenticationEntryPoint customAuthenticationEntryPoint() {
    	return (request, response, authException) -> {
    		// Detecto si es una petición API
    		String requestURI = request.getRequestURI();
    		String acceptHeader = request.getHeader("Accept");
    		
    		if (requestURI.startsWith("/api") ||
    				(acceptHeader != null && acceptHeader.contains("application/json"))) {
    			
    			// Respuesta JSON para API
    			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    			response.setContentType("application/json;charset=UTF-8");
    			
    			String message = determineAuthErrorMessage(authException);
    			String jsonResponse = createErrorJson("Unauthorized", message, 401, requestURI);
    			response.getWriter().write(jsonResponse);
    			
    		} else {
    			// Respuesta HTML para vistas
				response.sendRedirect("/auth/login?error=unauthorized");
    		}
    	};
    }
    
    // Manejador de excepción 403 (No autenticado)
    @Bean
    AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String requestURI = request.getRequestURI();
            String acceptHeader = request.getHeader("Accept");
            
            if (requestURI.startsWith("/api/") || 
                (acceptHeader != null && acceptHeader.contains("application/json"))) {
                
                // Respuesta JSON para APIs
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                
                String message = "Acceso denegado: No tienes los permisos necesarios para realizar esta operación";
                String jsonResponse = createErrorJson("Forbidden", message, 403, requestURI);
                response.getWriter().write(jsonResponse);
            } else {
                // Redirección a página de error para peticiones web
                response.sendRedirect("/auth/accessDenied");
            }
        };
    }
    
    private String determineAuthErrorMessage(AuthenticationException authException) {
        if (authException.getMessage().contains("Bad credentials")) {
            return "Credenciales inválidas. Verifica tu usuario y contraseña.";
        } else if (authException.getMessage().contains("User account is disabled")) {
            return "Cuenta de usuario deshabilitada.";
        } else if (authException.getMessage().contains("User account has expired")) {
            return "Cuenta de usuario expirada.";
        } else {
            return "Usuario no autenticado. Debes iniciar sesión para acceder a este recurso.";
        }
    }
    
    private String createErrorJson(String error, String message, int status, String requestPath) {
        return String.format(
            "{\"error\":\"%s\",\"message\":\"%s\",\"status\":%d,\"timestamp\":\"%s\",\"path\":\"%s\",\"user\":\"%s\"}",
            error, message, status, 
            java.time.Instant.now().toString(),
            requestPath,
            getCurrentUser()
        );
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
	
