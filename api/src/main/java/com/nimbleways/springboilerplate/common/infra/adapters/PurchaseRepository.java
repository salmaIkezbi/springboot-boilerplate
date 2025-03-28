package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.database.entities.PurchaseDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.entities.UserDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.jparepositories.JpaPurchaseRepository;
import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseRepository  implements PurchaseRepositoryPort {
    private final JpaPurchaseRepository jpaPurchaseRepository;
    private final UserRepositoryPort userRepository;

    public PurchaseRepository(final JpaPurchaseRepository jpaPurchaseRepository, UserRepositoryPort userRepository) {
        this.jpaPurchaseRepository = jpaPurchaseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ImmutableList<Purchase> findByUserID(UUID id) {
        userRepository.findByID(id);
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
        userRepository.findByID(purchaseToCreate.userId());

        PurchaseDbEntity purchaseDbEntity = PurchaseDbEntity.from(purchaseToCreate);
        UserDbEntity user = new UserDbEntity();
        user.id(purchaseToCreate.userId());

        purchaseDbEntity.user(user);
        PurchaseDbEntity savedPurchaseDbEntity = jpaPurchaseRepository.saveAndFlush(purchaseDbEntity);
        return savedPurchaseDbEntity.toPurchase();
    }

}

