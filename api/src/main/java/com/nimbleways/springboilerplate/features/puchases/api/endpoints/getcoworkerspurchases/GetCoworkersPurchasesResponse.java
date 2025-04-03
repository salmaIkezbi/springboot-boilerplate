package com.nimbleways.springboilerplate.features.puchases.api.endpoints.getcoworkerspurchases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.ArrayList;

public final class GetCoworkersPurchasesResponse extends ArrayList<GetCoworkersPurchasesResponse.Item> {
    public record Item(
            String id,
            String userId,
            String brand,
            String model,
            Double price,
            String store,
            ImmutableList<String> pathImage) {
    }

    public static GetCoworkersPurchasesResponse from(ImmutableList<Purchase> purchases) {
        GetCoworkersPurchasesResponse getCoworkersPurchasesResponse = new GetCoworkersPurchasesResponse(purchases.size());
        for (Purchase purchase : purchases) {
            getCoworkersPurchasesResponse.add(from(purchase));
        }
        return getCoworkersPurchasesResponse;
    }

    private GetCoworkersPurchasesResponse(int initialCapacity) {
        super(initialCapacity);
    }

    private static GetCoworkersPurchasesResponse.Item from(Purchase purchase) {
        return new GetCoworkersPurchasesResponse.Item(
                purchase.id().toString(),
                purchase.userId().toString(),
                purchase.brand(),
                purchase.model(),
                purchase.price(),
                purchase.store(),
                purchase.pathImage());
    }
}
