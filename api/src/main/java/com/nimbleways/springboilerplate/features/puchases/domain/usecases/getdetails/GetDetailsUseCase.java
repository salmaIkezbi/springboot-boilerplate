package com.nimbleways.springboilerplate.features.puchases.domain.usecases.getdetails;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;

import java.util.UUID;

public class GetDetailsUseCase {
    private final PurchaseRepositoryPort purchaseRepository;

    public GetDetailsUseCase(PurchaseRepositoryPort purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase handle(UUID id) {
        return purchaseRepository.getDetails(id);
    }
}
