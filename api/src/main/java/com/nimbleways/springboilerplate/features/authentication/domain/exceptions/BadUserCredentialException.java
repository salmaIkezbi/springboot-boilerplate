package com.nimbleways.springboilerplate.features.authentication.domain.exceptions;

public class BadUserCredentialException extends AbstractAuthenticationDomainException {
    public BadUserCredentialException(final String email) {
        super("Bad password provided for email: %s".formatted(email));
    }
}
