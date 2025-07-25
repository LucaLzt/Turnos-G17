package com.oo2.grupo17.exceptions;

public class EntidadNoEncontradaException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6357431496494091807L;

	public EntidadNoEncontradaException(String mensaje) {
		super(mensaje);
	}
}
