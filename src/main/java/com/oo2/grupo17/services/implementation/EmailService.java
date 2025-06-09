package com.oo2.grupo17.services.implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.oo2.grupo17.dtos.TurnoDto;
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
	
	public void enviarEmailRegistro(String email, String nombre) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Registro exitoso");
		message.setText(
	            "Hola " + nombre + ",\n\n" +
	            "Tu registro ha sido exitoso.\n\n" +
	            "Saludos,\n" +
	            "Equipo de Turnos-G17"
				);
		mailSender.send(message);
	}
	
	@Override
	public void enviarEmailConfirmacion(TurnoDto turno) {
		SimpleMailMessage cliente = new SimpleMailMessage();
		cliente.setTo(turno.getCliente().getContacto().getEmail());
		cliente.setSubject("Confirmación de Turno");
		cliente.setText(
	            "Hola " + turno.getCliente().getNombre() + ",\n\n" +
	            "Tu turno ha sido confirmado.\n" +
	            "Detalles del turno:\n" +
	            "Fecha y hora: " + turno.getDisponibilidad().getInicio() + "\n" +
	            "Duración: " + turno.getDisponibilidad().getDuracion() + "\n" +
	            "Profesional: " + turno.getProfesional().getNombre() + "\n" +
	            "Servicio: " + turno.getServicio().getNombre() + "\n\n" +
	            "Saludos,\n" +
	            "Equipo de Turnos-G17"
				);
		mailSender.send(cliente);
		
		SimpleMailMessage profesional = new SimpleMailMessage();
		profesional.setTo(turno.getProfesional().getContacto().getEmail());
		profesional.setSubject("Nuevo Turno Asignado");
		profesional.setText(
	            "Hola " + turno.getProfesional().getNombre() + ",\n\n" +
	            "Se te ha asignado un nuevo turno.\n" +
	            "Detalles del turno:\n" +
	            "Fecha y hora: " + turno.getDisponibilidad().getInicio() + "\n" +
	            "Duración: " + turno.getDisponibilidad().getDuracion() + "\n" +
	            "Cliente: " + turno.getCliente().getNombre() + "\n" +
	            "Servicio: " + turno.getServicio().getNombre() + "\n\n" +
	            "Saludos,\n" +
	            "Equipo de Turnos-G17"
				);
		mailSender.send(profesional);
	};
	
}
