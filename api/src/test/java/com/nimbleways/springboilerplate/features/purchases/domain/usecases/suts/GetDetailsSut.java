package com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts;

import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePurchaseRepository;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.usecases.getdetails.GetDetailsUseCase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Getter
@Import({
        GetDetailsUseCase.class,
        FakePurchaseRepository.class,
        FakeUserRepository.class
})
@RequiredArgsConstructor
public class GetDetailsSut {
    @Getter(AccessLevel.NONE)
    private final GetDetailsUseCase useCase;

    private final FakePurchaseRepository purchaseRepository;

    private final FakeUserRepository userRepository;

    public Purchase getDetails(UUID id) {
        return useCase.handle(id);
    }
}
