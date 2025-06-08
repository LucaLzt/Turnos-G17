package com.oo2.grupo17.services.implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.services.IEmailService;

import lombok.Builder;

@Service @Builder
public class EmailService implements IEmailService {
	
	private final JavaMailSender mailSender;
	
	@Override
	public void enviarEmailRegistro(String email, String nombre, String password) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Registro exitoso");
		message.setText(
	            "Hola " + nombre + ",\n\n" +
	            "Tu registro como profesional ha sido exitoso.\n" +
	            "Tu contraseña es: " + password + "\n\n" +
	            "Por favor, cambia tu contraseña al iniciar sesión por primera vez.\n\n" +
	            "Saludos,\n" +
	            "Equipo de Turnos-G17"
				);
		mailSender.send(message);
	}
	
	@Override
	public void enviarEmail(String email, String asunto, String mensaje) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(asunto + " - Servicio Turnos G17");
		message.setText(mensaje);
		mailSender.send(message);
	}

}
