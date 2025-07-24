package com.oo2.grupo17.dtos.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDto (
		
		@NotBlank @Email String username,
		@NotBlank String password
		
) {}
