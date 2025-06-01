package com.oo2.grupo17.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oo2.grupo17.dtos.ContactoDto;
import com.oo2.grupo17.entities.Contacto;

@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// Asegura que el id de persona (cliente) se setea en el id de ContactoDto
        modelMapper.typeMap(Contacto.class, ContactoDto.class)
            .addMappings(mapper -> mapper.map(src -> src.getPersona().getId(), ContactoDto::setId));
		
		return modelMapper;
	}
}
