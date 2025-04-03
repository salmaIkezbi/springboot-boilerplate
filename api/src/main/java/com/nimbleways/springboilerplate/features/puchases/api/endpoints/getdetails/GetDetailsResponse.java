package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getdetails;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import org.eclipse.collections.api.list.ImmutableList;

public record GetDetailsResponse(
        String id,
        String brand,
        String model,
        Double price,
        String store,
        ImmutableList<String> pathImage
) {
    public static GetDetailsResponse from(Purchase purchase) {
        return new GetDetailsResponse(
                purchase.id().toString(),
                purchase.brand(),
                purchase.model(),
                purchase.price(),
                purchase.store(),
                purchase.pathImage()
        );
    }
}
