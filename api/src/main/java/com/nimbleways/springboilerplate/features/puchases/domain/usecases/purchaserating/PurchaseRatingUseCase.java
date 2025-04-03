package com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;

public class PurchaseRatingUseCase {
    private final PurchaseRepositoryPort purchaseRepository;


    public PurchaseRatingUseCase(PurchaseRepositoryPort purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase handle(PurchaseRatingCommand command) {
        PurchaseRating purchaseRating = command.toPurchaseRating();
        return purchaseRepository.ratePurchase(purchaseRating);
    }
}
