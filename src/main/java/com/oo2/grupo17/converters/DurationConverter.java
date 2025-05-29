package com.oo2.grupo17.converters;

import java.time.Duration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
	
	@Override
	public Long convertToDatabaseColumn(Duration duration) {
		return duration != null ? duration.toMinutes() : null;
	}
	
	@Override
	public Duration convertToEntityAttribute(Long dbData) {
		return dbData != null ? Duration.ofMinutes(dbData) : null;
	}
	
}
