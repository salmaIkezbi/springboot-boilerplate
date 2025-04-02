package com.nimbleways.springboilerplate.testhelpers.fixtures;

import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import org.eclipse.collections.api.list.ImmutableList;
import org.jilt.Builder;

import java.util.UUID;

import static java.util.Objects.requireNonNullElse;

public class NewPurchaseFixture {
    private static final ImmutableList<String> DEFAULT_IMAGES = Immutable.list.of();
    @Builder(factoryMethod = "aNewPurchase")
    public static NewPurchase buildNewUser( UUID userId,
                                           String brand,
                                           String model,
                                           Double price,
                                           String store,
                                           ImmutableList<String> pathImage, Integer rate) {
        UUID id = UUID.randomUUID();
        String brandValue = requireNonNullElse(brand, "brand-" + id);
        String modelValue = requireNonNullElse(model, "model-" + id);
        Double priceValue = requireNonNullElse(price, 0.0);
        String storeValue = requireNonNullElse(store, "store-" + id);
        UUID userIdValue = requireNonNullElse(userId, id);
        ImmutableList<String> pathImageValue = requireNonNullElse(pathImage, DEFAULT_IMAGES);
        Integer rateValue = requireNonNullElse(rate, 0);

        return new NewPurchase(userIdValue, brandValue, modelValue, priceValue, storeValue, pathImageValue, rateValue);
    }
}
