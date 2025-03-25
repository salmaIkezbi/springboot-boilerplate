package com.nimbleways.springboilerplate.features.users.domain.usecases.getuser;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;

import java.util.UUID;

public class GetUserUseCase {

    private final UserRepositoryPort userRepository;

    public GetUserUseCase(final UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User handle(UUID id) {
        return userRepository.findByID(id);
    }
}
