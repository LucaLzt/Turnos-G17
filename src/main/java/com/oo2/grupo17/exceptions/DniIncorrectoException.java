package com.oo2.grupo17.exceptions;

public class DniIncorrectoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4972349516020630462L;

	public DniIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
