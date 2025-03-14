package com.nimbleways.springboilerplate.features.users.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.GetUsersSut;

import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

@UnitTest
class GetUsersUseCaseUnitTests {
    private final GetUsersSut sut = Instance.create(GetUsersSut.class);

    @Test
    void returns_existing_users_in_repository() {
        // GIVEN
        User user1 = sut.userRepository().create(aNewUser().build());
        User user2 = sut.userRepository().create(aNewUser().build());

        // WHEN
        ImmutableList<User> usersInRepository = sut.getUsers();

        // THEN
        assertThat(usersInRepository).containsExactlyInAnyOrder(user1, user2);
    }
}
