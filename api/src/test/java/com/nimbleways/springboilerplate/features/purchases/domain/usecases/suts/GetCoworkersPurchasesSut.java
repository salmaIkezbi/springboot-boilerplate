package com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts;

import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePurchaseRepository;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getcoworkerspurchases.GetCoworkersPurchasesUseCase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Getter
@Import({
        GetCoworkersPurchasesUseCase.class,
        FakePurchaseRepository.class,
        FakeUserRepository.class
})
@RequiredArgsConstructor
public class GetCoworkersPurchasesSut {
    @Getter(AccessLevel.NONE)
    private final GetCoworkersPurchasesUseCase useCase;

    private final FakePurchaseRepository fakePurchaseRepository;

    private final FakeUserRepository fakeUserRepository;

    public ImmutableList<Purchase> getCoworkersPurchases(UUID id){ return useCase.handle(id);}
}
