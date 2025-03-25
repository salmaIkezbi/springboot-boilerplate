package com.nimbleways.springboilerplate.features.authentication.domain.exceptions;

public class UnknownEmailException extends AbstractAuthenticationDomainException {
    public UnknownEmailException(final String email) {
        super("email not found: %s".formatted(email));
    }
}
