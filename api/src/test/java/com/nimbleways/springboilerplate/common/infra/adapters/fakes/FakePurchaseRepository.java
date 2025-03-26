package com.nimbleways.springboilerplate.common.infra.adapters.fakes;


import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
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

}
