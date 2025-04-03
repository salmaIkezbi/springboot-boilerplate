package com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating;

import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;

import java.util.UUID;

public record PurchaseRatingCommand(
        UUID purchase_id,
        int rating
) {
    public PurchaseRating toPurchaseRating() {
        return new PurchaseRating(
                purchase_id(),
                rating()
                );
    }
}
