package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
