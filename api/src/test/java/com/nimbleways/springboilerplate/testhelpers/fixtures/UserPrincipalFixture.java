package com.nimbleways.springboilerplate.testhelpers.fixtures;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;
import java.util.UUID;

import jakarta.annotation.Nullable;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jilt.Builder;

import static java.util.Objects.requireNonNullElse;

public class UserPrincipalFixture {
    private static final String DEFAULT_EMAIL = "email";
    private static final ImmutableSet<Role> DEFAULT_ROLES = Immutable.set.of();

    @Builder(factoryMethod = "aUserPrincipal")
    public static UserPrincipal buildUserPrincipal(
            @Nullable UUID id,
            @Nullable String email,
            @Nullable ImmutableSet<Role> roles) {
        UUID idValue = requireNonNullElse(id, UUID.randomUUID());
        String emailValue = requireNonNullElse(email, DEFAULT_EMAIL);
        ImmutableSet<Role> rolesValue = requireNonNullElse(roles, DEFAULT_ROLES);

        return new UserPrincipal(
                idValue,
                emailValue,
                rolesValue);
    }
}
