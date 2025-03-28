package com.nimbleways.springboilerplate.features.puchases.domain.usecases.getpurchase;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;

public class GetPurchaseUseCase {
    private final PurchaseRepositoryPort purchaseRepository;

    public GetPurchaseUseCase(final PurchaseRepositoryPort purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public ImmutableList<Purchase> handle(UUID id) {
        return purchaseRepository.findByUserID(id);
    }

}
