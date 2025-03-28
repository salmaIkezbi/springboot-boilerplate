package com.nimbleways.springboilerplate.features.users.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.GetUserSut;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.junit.jupiter.api.Test;

import java.util.UUID;


@UnitTest
class GetUserUseCaseUnitTests {

    private final GetUserSut sut = Instance.create(GetUserSut.class);

    @Test
    void returns_existing_user_in_repository() {
        // GIVEN
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        User user = sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());

        // WHEN
        User retrievedUser = sut.getUser(user.id());
        // THEN
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.id()).isEqualTo(user.id());
        assertThat(retrievedUser.name()).isEqualTo(user.name());
        assertThat(retrievedUser.email().value()).isEqualTo(user.email().value());
    }

    @Test
    void getting_user_with_random_uuid() {
        // GIVEN : Création d'un nouvel utilisateur avec un UUID aléatoire
        UUID randomUuid = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class,
                () -> sut.getUser(randomUuid));

        assertEquals(UserNotFoundInRepositoryException.class, exception.getClass());
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertEquals("User with ID " + randomUuid.toString() + " not found", exception.getMessage());

    }



}
