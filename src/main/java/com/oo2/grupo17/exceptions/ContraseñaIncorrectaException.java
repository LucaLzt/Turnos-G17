package com.oo2.grupo17.exceptions;

public class ContraseñaIncorrectaException extends RuntimeException {
    public ContraseñaIncorrectaException(String mensaje) {
        super(mensaje);
    }
}