package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.database.entities.PurchaseDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.entities.UserDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.jparepositories.JpaPurchaseRepository;
import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.EmailAlreadyExistsInRepositoryException;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseRepository  implements PurchaseRepositoryPort {
    private final JpaPurchaseRepository jpaPurchaseRepository;

    public PurchaseRepository(final JpaPurchaseRepository jpaPurchaseRepository) {
        this.jpaPurchaseRepository = jpaPurchaseRepository;
    }

    @Override
    public ImmutableList<Purchase> findByUserID(UUID id) {
        return Immutable.list.ofAll(jpaPurchaseRepository.findByuserId(id)
                .stream()
                .map(PurchaseDbEntity::toPurchase)
                .toList());
    }

}

