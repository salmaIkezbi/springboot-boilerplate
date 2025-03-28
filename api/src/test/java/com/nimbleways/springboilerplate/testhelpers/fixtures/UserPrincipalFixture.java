package com.nimbleways.springboilerplate.testhelpers.fixtures;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;

import java.util.UUID;

import jakarta.annotation.Nullable;
import org.jilt.Builder;

import static java.util.Objects.requireNonNullElse;

public class UserPrincipalFixture {
    private static final String DEFAULT_EMAIL = "email";
    private static final String DEFAULT_ROLE = "USER";

    @Builder(factoryMethod = "aUserPrincipal")
    public static UserPrincipal buildUserPrincipal(
            @Nullable UUID id,
            @Nullable String email,
            @Nullable String role) {
        UUID idValue = requireNonNullElse(id, UUID.randomUUID());
        String emailValue = requireNonNullElse(email, DEFAULT_EMAIL);
        String roleValue = requireNonNullElse(role, DEFAULT_ROLE);
        return new UserPrincipal(
                idValue,
                new Email(emailValue),
                roleValue);
    }
}
