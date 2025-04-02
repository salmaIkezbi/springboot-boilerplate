package com.nimbleways.springboilerplate.features.puchases.domain.valueobjects;

import java.util.UUID;

public record PurchaseRating(
        UUID purchaseId,
        Integer rating
) {
}
