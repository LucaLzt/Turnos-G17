package com.oo2.grupo17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Turnos G17 API")
				.description("""
						API documentation para la aplicación Turnos G17
						
	                    ## Autenticación
	                    Esta API utiliza **HTTP Basic Authentication**.
	                    
	                    ## Roles disponibles:
	                    - **CLIENTE**: Acceso a endpoints de cliente (`/api/clientes/**`)
	                    - **PROFESIONAL**: Acceso a endpoints de profesional (`/api/profesionales/**`)
	                    - **ADMINISTRADOR**: Acceso a endpoints de admin (`/api/admin/**`)
	                    
	                    ## Códigos de error comunes:
	                    - **401**: Usuario no autenticado (credenciales faltantes o inválidas)
	                    - **403**: Acceso denegado (sin permisos para el rol requerido)
	                    - **404**: Recurso no encontrado
	                    - **422**: Error en validación de datos
	                    - **500**: Error interno del servidor
	                    """)
				.version("1.0.0")
				.contact(new Contact().name("Grupo 17"))
				)
			.addSecurityItem(new SecurityRequirement().addList("basicAuth"))
			.components(
				new Components()
	            .addSecuritySchemes("basicAuth", 
	            	new SecurityScheme()
	                .type(SecurityScheme.Type.HTTP)
	                .scheme("basic")
	            )
	        );
	}
	
} 
