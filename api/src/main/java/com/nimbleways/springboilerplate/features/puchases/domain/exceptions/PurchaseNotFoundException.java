package com.nimbleways.springboilerplate.features.puchases.domain.exceptions;

import com.nimbleways.springboilerplate.common.domain.exceptions.AbstractDomainException;

public class PurchaseNotFoundException extends AbstractDomainException {
    public PurchaseNotFoundException(String id, Throwable cause) {
        super("Purchase with ID %s not found".formatted(id), cause);
    }
}
