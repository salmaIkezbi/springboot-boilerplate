package com.nimbleways.springboilerplate.features.puchases.domain.ports;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;


public interface PurchaseRepositoryPort {
    ImmutableList<Purchase> findByUserID(UUID id);
    ImmutableList<Purchase> findAll();
    Purchase create(NewPurchase purchaseToCreate);
    Purchase getDetails(UUID id);
    Purchase ratePurchase(PurchaseRating purchaseRating);
}
