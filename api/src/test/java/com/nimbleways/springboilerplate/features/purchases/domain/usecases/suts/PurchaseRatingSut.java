package com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts;


import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePurchaseRepository;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating.PurchaseRatingCommand;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.purchaserating.PurchaseRatingUseCase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;


@Getter
@Import({
        PurchaseRatingUseCase.class,
        FakePurchaseRepository.class,
        FakeUserRepository.class
})
@RequiredArgsConstructor
public class PurchaseRatingSut {
    @Getter(AccessLevel.NONE)
    private final PurchaseRatingUseCase useCase;

    private final FakePurchaseRepository purchaseRepository;

    private final FakeUserRepository userRepository;

    public Purchase rate(final PurchaseRatingCommand purchaseRatingCommand) {
        return useCase.handle(purchaseRatingCommand);
    }
}
