package com.nimbleways.springboilerplate.features.users.domain.usecases.suts;


import com.nimbleways.springboilerplate.common.domain.ports.TimeProviderPort;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePasswordEncoder;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeUserRepository;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUseCase;
import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUserCommand;
import com.nimbleways.springboilerplate.testhelpers.configurations.TimeTestConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;

@Getter
@Import({
        UpdateUseCase.class,
        FakeUserRepository.class,
        TimeTestConfiguration.class,
        FakePasswordEncoder.class
})
@RequiredArgsConstructor
public class UpdateSut {
    @Getter(AccessLevel.NONE)
    private final UpdateUseCase useCase;

    private final FakeUserRepository userRepository;
    private final FakePasswordEncoder passwordEncoder;
    private final TimeProviderPort timeProvider;

    public User update(final UpdateUserCommand updateUserCommand) {
        return useCase.handle(updateUserCommand);
    }
}
