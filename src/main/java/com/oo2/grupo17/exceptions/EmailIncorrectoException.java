package com.oo2.grupo17.exceptions;

public class EmailIncorrectoException extends RuntimeException {
    public EmailIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
