package com.oo2.grupo17.exceptions;

public class RolNoEncontradoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3628478593751995508L;

	public RolNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}