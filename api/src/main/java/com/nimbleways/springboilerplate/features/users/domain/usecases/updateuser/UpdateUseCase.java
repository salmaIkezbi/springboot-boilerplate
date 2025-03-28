package com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser;

import com.nimbleways.springboilerplate.common.domain.ports.PasswordEncoderPort;
import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.UpdatedUser;

public class UpdateUseCase {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdateUseCase(
            final UserRepositoryPort userRepository,
            final PasswordEncoderPort passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handle(final UpdateUserCommand updateUserCommand) {
        EncodedPassword encodedPassword = passwordEncoder.encode(updateUserCommand.password());
        UpdatedUser updatedUser = updateUserCommand.toUpdatedUser(encodedPassword);
        return userRepository.update(updatedUser);
    }
}
