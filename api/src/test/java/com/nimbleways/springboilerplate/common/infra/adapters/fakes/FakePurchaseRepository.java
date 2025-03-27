package com.nimbleways.springboilerplate.common.infra.adapters.fakes;


import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.context.annotation.Import;

import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Import(FakeDatabase.class)
public class FakePurchaseRepository implements PurchaseRepositoryPort {
    private final FakeDatabase fakeDb;

    @Override
    public ImmutableList<Purchase> findByUserID(UUID userId) {
        return Immutable.list.ofAll(fakeDb.purchaseTable
                .values()
                .stream()
                .filter(p -> p.userId().equals(userId))
                .collect(Collectors.toList()));
    }

    @Override
    public Purchase create(NewPurchase purchaseToCreate) {
        if (!userExists(purchaseToCreate.userId())) {
            throw new UserNotFoundInRepositoryException(purchaseToCreate.userId().toString() , new IllegalArgumentException("user not found")  );
        }
        Purchase purchase = toPurchase(purchaseToCreate);
        fakeDb.purchaseTable.put(purchase.id().toString(), purchase);
        return purchase;
    }

    @Override
    public ImmutableList<Purchase> findAll() {
        return Immutable.list.ofAll(fakeDb.purchaseTable.values());
    }
    private boolean userExists(UUID userId) {
        return fakeDb.userTable.values().stream()
                .anyMatch(userWithPassword -> userWithPassword.user().id().equals(userId));
    }

    private static Purchase toPurchase(NewPurchase purchaseToCreate) {
        return new Purchase(
                UUID.randomUUID(),
                purchaseToCreate.userId(),
                purchaseToCreate.brand(),
                purchaseToCreate.model(),
                purchaseToCreate.price(),
                purchaseToCreate.store(),
                purchaseToCreate.pathImages()
        );
    }




}
