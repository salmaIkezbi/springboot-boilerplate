package com.nimbleways.springboilerplate.features.users.domain.usecases;

import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.UpdateSut;
import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUserCommand;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class UpdateUseCaseUnitTests {

    private final UpdateSut sut = Instance.create(UpdateSut.class);

    @Test
    void update_a_user_in_repository() {

        // GIVEN
        UpdateUserCommand updateUserCommand = createUpdateUserCommand();

        // WHEN
        User user = sut.update(updateUserCommand);

        // THEN
        ImmutableList<User> users = sut.userRepository().findAll();
        assertEquals(List.of(user), users);
    }

    @Test
    void update_usercredentials_in_repository() {
        // GIVEN

        UpdateUserCommand updateUserCommand = createUpdateUserCommand();


        // WHEN
        sut.update(updateUserCommand);


        // THEN
        EncodedPassword encodedPassword = getPasswordFromRepository(updateUserCommand.id());
        boolean passwordMatches = sut.passwordEncoder()
                .matches(updateUserCommand.password(), encodedPassword);
        assertTrue(passwordMatches);
    }

    private EncodedPassword getPasswordFromRepository(UUID id) {
        User user =  sut
                .userRepository()
                .findByID(id);

        return sut
                .userRepository()
                .findUserCredentialByEmail(user.email().value())
                .orElseThrow()
                .encodedPassword();

    }

    @Test
    void update_returns_the_updated_user() {
        // GIVEN
        UpdateUserCommand updateUserCommand = createUpdateUserCommand();
        User expectedUser = getUser(updateUserCommand);

        // WHEN
        User user = sut.update(updateUserCommand);

        // THEN
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id", "roles.id")
                .isEqualTo(expectedUser);
    }

    private User getUser(UpdateUserCommand updateUserCommand) {
        User user = sut.userRepository().findByID(updateUserCommand.id());
        return new User(
                user.id(),
                user.name(),
                user.email(),
                user.createdAt(),
                user.role(),
                user.employmentDate(),
                updateUserCommand.shouldReceiveMailNotifications(),
                updateUserCommand.shouldReceiveApprovalNotifications());
    }

    private UpdateUserCommand createUpdateUserCommand() {
        User newUser = addNewUserToRepository();
        return new UpdateUserCommand(
                "password",
                true,
                true,
                newUser.id()
        );
    }

    private User addNewUserToRepository() {
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        return sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());
    }
}
