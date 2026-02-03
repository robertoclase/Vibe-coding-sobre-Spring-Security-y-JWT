package com.salesianostriana.dam.SpringSecurityyJWT.springsecurityyjwt.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
