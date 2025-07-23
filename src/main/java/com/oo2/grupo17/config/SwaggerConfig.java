package com.oo2.grupo17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Turnos G17 API")
					.version("1.0.0")
					.description("API documentation for Turnos G17 application")
					.contact(new Contact().name("Grupo 17"))
					.license(new License().name("MIT").url("https://opensource.org/license/mit/"))
					)
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
