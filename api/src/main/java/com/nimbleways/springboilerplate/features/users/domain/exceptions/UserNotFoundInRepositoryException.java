package com.nimbleways.springboilerplate.features.users.domain.exceptions;

import com.nimbleways.springboilerplate.common.domain.exceptions.AbstractDomainException;

public class UserNotFoundInRepositoryException extends AbstractDomainException {
    public UserNotFoundInRepositoryException(String id, Throwable cause) {
        super("User with ID %s not found".formatted(id), cause);
    }
}
