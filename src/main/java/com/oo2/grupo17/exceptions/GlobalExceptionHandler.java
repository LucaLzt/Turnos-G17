package com.oo2.grupo17.exceptions;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

	@ExceptionHandler(EntidadNoEncontradaException.class)
	public String handleEntidadNoEncontrada(EntidadNoEncontradaException ex, Model model) {
		model.addAttribute("mensaje", ex.getMessage());
		return "error/personalizados/entidad-no-encontrada";
	}
	
    @ExceptionHandler(DniIncorrectoException.class)
    public String handleDniIncorrecto(DniIncorrectoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/personalizados/credencial-incorrecta";
    }

    @ExceptionHandler(ContraseñaIncorrectaException.class)
    public String handleContraseñaIncorrecta(ContraseñaIncorrectaException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/personalizados/credencial-incorrecta";
    }

    @ExceptionHandler(EmailIncorrectoException.class)
    public String handleEmailIncorrecto(EmailIncorrectoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/personalizados/credencial-incorrecta";
    }
    
    @ExceptionHandler(RolNoEncontradoException.class)
    public String handleEmailIncorrecto(RolNoEncontradoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/personalizados/rol-no-encontrado";
    }
    
    @ExceptionHandler(EntidadDuplicadaException.class)
    public String manejarEntidadDuplicada(EntidadDuplicadaException ex, Model model) {
        model.addAttribute("errorDuplicado", ex.getMessage());
        return "error/personalizados/elemento-duplicado";
    }
	
}
