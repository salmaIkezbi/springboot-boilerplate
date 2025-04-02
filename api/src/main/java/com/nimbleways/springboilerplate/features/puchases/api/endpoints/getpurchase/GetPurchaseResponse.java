package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getpurchase;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.ArrayList;

public final class GetPurchaseResponse extends ArrayList<GetPurchaseResponse.Item> {
    public record Item(
                    String id,
                    String userId,
            String brand,
            String model,
            Double price,
            String store,
            ImmutableList<String> pathImage) {
    }

    public static GetPurchaseResponse from(ImmutableList<Purchase> purchases) {
        GetPurchaseResponse getPurchaseResponse = new GetPurchaseResponse(purchases.size());
        for (Purchase purchase : purchases) {
            getPurchaseResponse.add(from(purchase));
        }
        return getPurchaseResponse;
    }

    private GetPurchaseResponse(int initialCapacity) {
        super(initialCapacity);
    }

    private static Item from(Purchase purchase) {
        return new Item(
                purchase.id().toString(),
                purchase.userId().toString(),
                purchase.brand(),
                purchase.model(),
                purchase.price(),
                purchase.store(),
                purchase.pathImage());
    }
}
