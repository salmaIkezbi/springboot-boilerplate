package com.nimbleways.springboilerplate.features.users.domain.exceptions;

import com.nimbleways.springboilerplate.common.domain.exceptions.AbstractDomainException;

public class EmailAlreadyExistsInRepositoryException extends AbstractDomainException {

    public EmailAlreadyExistsInRepositoryException(String email, Throwable cause) {
        super("email '%s' already exist in repository".formatted(email), cause);
    }
}
