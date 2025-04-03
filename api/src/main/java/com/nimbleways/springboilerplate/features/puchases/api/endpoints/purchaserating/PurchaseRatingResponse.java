package com.nimbleways.springboilerplate.features.puchases.api.endpoints.purchaserating;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import org.eclipse.collections.api.list.ImmutableList;

public record PurchaseRatingResponse(
        String id,
        String brand,
        String model,
        Double price,
        String store,
        ImmutableList<String> pathImage,
        Integer rate
) {
    public static PurchaseRatingResponse from(Purchase purchase) {
        return new PurchaseRatingResponse(
                purchase.id().toString(),
                purchase.brand(),
                purchase.model(),
                purchase.price(),
                purchase.store(),
                purchase.pathImage(),
                purchase.rate()
                );
    }
}
