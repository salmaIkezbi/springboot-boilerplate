package com.nimbleways.springboilerplate.features.puchases.domain.usecases.getcoworkerspurchases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;

public class GetCoworkersPurchasesUseCase {
    private final PurchaseRepositoryPort purchaseRepository;

    public GetCoworkersPurchasesUseCase(PurchaseRepositoryPort purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public ImmutableList<Purchase> handle(UUID id ){
        return purchaseRepository.FindByUser_IdNot(id);
    }
}
