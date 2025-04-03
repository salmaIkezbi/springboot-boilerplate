package com.nimbleways.springboilerplate.features.puchases.api.endpoints.purchaserating;

import com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating.PurchaseRatingCommand;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RatingRequest(
        @NotNull Integer rating
) {
    PurchaseRatingCommand toPurchaseRatingCommand(UUID purchaseId)
    {
        return new PurchaseRatingCommand(purchaseId,rating);
    }
}
