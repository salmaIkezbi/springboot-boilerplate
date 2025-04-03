package com.nimbleways.springboilerplate.features.users.domain.usecases.suts;

import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.getuser.GetUserUseCase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@Getter
@Import({
        GetUserUseCase.class,
        FakeUserRepository.class
})
@RequiredArgsConstructor
public class GetUserSut {
    @Getter(AccessLevel.NONE)
    private final GetUserUseCase useCase;

    private final FakeUserRepository userRepository;

    public User getUser(UUID id) {
        return useCase.handle(id);
    }
}
