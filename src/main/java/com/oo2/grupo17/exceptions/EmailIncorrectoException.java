package com.oo2.grupo17.exceptions;

public class EmailIncorrectoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8871988668098205485L;

	public EmailIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
