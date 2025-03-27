package com.nimbleways.springboilerplate.features.puchases.domain.valueobjects;

import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;

public record NewPurchase(
        UUID userId,
        String brand,
        String model,
        Double price,
        String store,
        ImmutableList<String> pathImages
) {
}
