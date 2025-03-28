package com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts;


import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePurchaseRepository;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getpurchase.GetPurchaseUseCase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Getter
@Import({
        GetPurchaseUseCase.class,
        FakePurchaseRepository.class,
        FakeUserRepository.class
})
@RequiredArgsConstructor
public class GetPurchaseSut {
    @Getter(AccessLevel.NONE)
    private final GetPurchaseUseCase useCase;

    private final FakePurchaseRepository purchaseRepository;

    private final FakeUserRepository userRepository;


    public ImmutableList<Purchase> getPurchase(UUID id) {
        return useCase.handle(id);
    }
}
