package com.nimbleways.springboilerplate.features.authentication.domain.exceptions;

import com.nimbleways.springboilerplate.common.domain.exceptions.AbstractDomainException;

public class CannotCreateUserSessionInRepositoryException extends AbstractDomainException {

    public CannotCreateUserSessionInRepositoryException(String email, Throwable cause) {
        super(
                "Cannot create UserSession in repository for user '%s'".formatted(email),
                cause);
    }

}
