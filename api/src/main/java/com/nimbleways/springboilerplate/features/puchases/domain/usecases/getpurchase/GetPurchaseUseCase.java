package com.nimbleways.springboilerplate.features.puchases.domain.usecases.getpurchase;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.UUID;

public class GetPurchaseUseCase {
    private final PurchaseRepositoryPort purchaseRepository;
    private final UserRepositoryPort userRepository;

    public GetPurchaseUseCase(final PurchaseRepositoryPort purchaseRepository, UserRepositoryPort userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
    }

    public ImmutableList<Purchase> handle(UUID id) {
        userRepository.findByID(id).orElseThrow(() -> new UserNotFoundInRepositoryException(id.toString(),new IllegalArgumentException("user not found")));
        return purchaseRepository.findByUserID(id);
    }

}
