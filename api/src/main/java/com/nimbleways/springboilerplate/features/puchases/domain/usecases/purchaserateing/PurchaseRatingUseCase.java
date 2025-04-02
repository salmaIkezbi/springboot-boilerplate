package com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserateing;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;


public class PurchaseRatingUseCase {

    private final PurchaseRepositoryPort purchaseRepository;

    public PurchaseRatingUseCase(final PurchaseRepositoryPort purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase handle(PurchaseRating purchaseRating) {
        return purchaseRepository.ratePurchase(purchaseRating);
    }
}
