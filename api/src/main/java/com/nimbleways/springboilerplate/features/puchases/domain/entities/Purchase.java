package com.nimbleways.springboilerplate.features.puchases.domain.entities;


import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;

public record Purchase(
        UUID id,
        UUID userId,
        String brand,
        String model,
        Double price,
        String store,
        ImmutableList<String> pathImage
) {

}
