package com.oo2.grupo17.exceptions;

public class EntidadDuplicadaException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6286205232431566759L;

	public EntidadDuplicadaException(String mensaje) {
		super(mensaje);
	}
}
