package com.nimbleways.springboilerplate.features.authentication.domain.ports;

import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserCredential;
import java.util.Optional;

public interface UserCredentialsRepositoryPort {

    Optional<UserCredential> findUserCredentialByEmail(String email);
}
