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
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

}
