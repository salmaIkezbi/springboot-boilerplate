package com.nimbleways.springboilerplate.common.infra.adapters.fakes;


import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.exceptions.PurchaseNotFoundException;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.context.annotation.Import;

import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Import(FakeDatabase.class)
public class FakePurchaseRepository implements PurchaseRepositoryPort {
    private final FakeDatabase fakeDb;
    private final UserRepositoryPort userRepository;

    @Override
    public ImmutableList<Purchase> findByUserID(UUID userId) {
        if(userRepository.findByID(userId) == null) {
            throw new UserNotFoundInRepositoryException(userId.toString() , new IllegalArgumentException("user not found")  );
        }
        return Immutable.list.ofAll(fakeDb.purchaseTable
                .values()
                .stream()
                .filter(p -> p.userId().equals(userId))
                .collect(Collectors.toList()));
    }

    @Override
    public Purchase create(NewPurchase purchaseToCreate) {
        if (userRepository.findByID(purchaseToCreate.userId()) == null) {
            throw new UserNotFoundInRepositoryException(purchaseToCreate.userId().toString() , new IllegalArgumentException("user not found")  );
        }
        Purchase purchase = toPurchase(purchaseToCreate);
        fakeDb.purchaseTable.put(purchase.id().toString(), purchase);
        return purchase;
    }

    @Override
    public Purchase getDetails(UUID id) {
        Purchase purchase = fakeDb.purchaseTable.get(id.toString());
        if(purchase == null) {
            throw new PurchaseNotFoundException(id.toString() , new IllegalArgumentException("purchase not found")  );
        }
        return purchase;
    }

    @Override
    public Purchase ratePurchase(PurchaseRating purchaseRating) {
        return fakeDb.purchaseTable
                .values()
                .stream()
                .filter(purchase -> purchase.id().equals(purchaseRating.purchaseId()))
                .findFirst()
                .map(existingPurchase -> {
                    Purchase updatedpurchase = new Purchase(
                            existingPurchase.id(),
                            existingPurchase.userId(),
                            existingPurchase.brand(),
                            existingPurchase.model(),
                            existingPurchase.price(),
                            existingPurchase.store(),
                            existingPurchase.pathImage(),
                            purchaseRating.rating()
                    );
                    fakeDb.purchaseTable.remove(existingPurchase.id().toString());
                    fakeDb.purchaseTable.put(existingPurchase.id().toString(),updatedpurchase);
                    return updatedpurchase;
                })
                .orElseThrow(() -> new PurchaseNotFoundException(purchaseRating.purchaseId().toString(),
                        new IllegalArgumentException("ID purchase invalide : " + purchaseRating.purchaseId())));


    }

    @Override
    public ImmutableList<Purchase> FindByUser_IdNot(UUID id) {
        return Immutable.list.ofAll(fakeDb.purchaseTable
                .values()
                .stream()
                .filter(purchase -> ! purchase.userId().equals(id))
                .collect(Collectors.toList()));
    }

    @Override
    public ImmutableList<Purchase> findAll() {
        return Immutable.list.ofAll(fakeDb.purchaseTable.values());
    }


    private static Purchase toPurchase(NewPurchase purchaseToCreate) {
        return new Purchase(
                UUID.randomUUID(),
                purchaseToCreate.userId(),
                purchaseToCreate.brand(),
                purchaseToCreate.model(),
                purchaseToCreate.price(),
                purchaseToCreate.store(),
                purchaseToCreate.pathImages(),
                0
        );
    }




}
