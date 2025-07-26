package com.oo2.grupo17.exceptions;

public class ContraseñaIncorrectaException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9060606512094070797L;

	public ContraseñaIncorrectaException(String mensaje) {
        super(mensaje);
    }
}