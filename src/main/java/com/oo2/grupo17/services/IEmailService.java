package com.oo2.grupo17.services;

import com.oo2.grupo17.dtos.TurnoDto;

public interface IEmailService {
	
	void enviarEmailRegistro(String email, String nombre, String password);

	void enviarEmail(String email, String asunto, String mensaje);

	void enviarEmailConfirmacion(TurnoDto turno);
	
}
