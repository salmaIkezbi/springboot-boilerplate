package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.database.entities.PurchaseDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.entities.UserDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.jparepositories.JpaPurchaseRepository;
import com.nimbleways.springboilerplate.common.infra.database.jparepositories.JpaUserRepository;
import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseRepository  implements PurchaseRepositoryPort {
    private final JpaPurchaseRepository jpaPurchaseRepository;
    private final JpaUserRepository jpaUserRepository;
    public PurchaseRepository(final JpaPurchaseRepository jpaPurchaseRepository, JpaUserRepository jpaUserRepository) {
        this.jpaPurchaseRepository = jpaPurchaseRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public ImmutableList<Purchase> findByUserID(UUID id) {
        return Immutable.list.ofAll(jpaPurchaseRepository.findByuserId(id)
                .stream()
                .map(PurchaseDbEntity::toPurchase)
                .toList());
    }

    @Override
    public ImmutableList<Purchase> findAll() {
        return Immutable.collectList(jpaPurchaseRepository.findAll(), PurchaseDbEntity::toPurchase);
    }

    @Override
    public Purchase create(NewPurchase purchaseToCreate) {
        UserDbEntity userDbEntity = jpaUserRepository.findById(purchaseToCreate.userId())
                .orElseThrow(() -> new UserNotFoundInRepositoryException(purchaseToCreate.userId().toString(),new IllegalArgumentException("bad user id ")));

        PurchaseDbEntity purchaseDbEntity = PurchaseDbEntity.from(purchaseToCreate);
        purchaseDbEntity.user(userDbEntity);
        PurchaseDbEntity savedPurchaseDbEntity;
        savedPurchaseDbEntity = jpaPurchaseRepository.saveAndFlush(purchaseDbEntity);
        return savedPurchaseDbEntity.toPurchase();
    }

}

