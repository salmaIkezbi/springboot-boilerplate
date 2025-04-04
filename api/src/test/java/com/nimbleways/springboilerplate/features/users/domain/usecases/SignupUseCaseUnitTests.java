package com.nimbleways.springboilerplate.features.users.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.entities.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.signup.SignupCommand;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.SignupSut;
import java.util.List;
import java.util.UUID;

import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

@UnitTest
class SignupUseCaseUnitTests {

    private final SignupSut sut = Instance.create(SignupSut.class);

    @Test
    void signup_create_a_new_user_in_repository() {
        // GIVEN
        SignupCommand signupCommand = createSignupCommand();

        // WHEN
        User user = sut.signup(signupCommand);

        // THEN
        ImmutableList<User> users = sut.userRepository().findAll();
        assertEquals(List.of(user), users);
    }

    @Test
    void signup_returns_the_created_user() {
        // GIVEN
        SignupCommand signupCommand = createSignupCommand();
        User expectedUser = getUser(signupCommand);

        // WHEN
        User user = sut.signup(signupCommand);

        // THEN
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id", "roles.id")
                .isEqualTo(expectedUser);
    }

    @Test
    void signup_create_usercredentials_in_repository() {
        // GIVEN
        SignupCommand signupCommand = createSignupCommand();

        // WHEN
        sut.signup(signupCommand);

        // THEN
        EncodedPassword encodedPassword = getPasswordFromRepository(signupCommand.email().value());
        boolean passwordMatches = sut.passwordEncoder()
                .matches(signupCommand.plainPassword(), encodedPassword);
        assertTrue(passwordMatches);
    }

    private EncodedPassword getPasswordFromRepository(String email) {
        return sut
                .userRepository()
                .findUserCredentialByEmail(email)
                .orElseThrow()
                .encodedPassword();
    }

    private User getUser(SignupCommand signupCommand) {
        return new User(
                UUID.randomUUID(),
                signupCommand.name(),
                signupCommand.email(),
                sut.timeProvider().instant(),
                signupCommand.role(),
                signupCommand.employmentDate(),
                false,
                false);
    }

    private static SignupCommand createSignupCommand() {
        User inputUser = aUser().role(String.valueOf(Role.ADMIN)).build();
        return new SignupCommand(inputUser.name(),
                inputUser.email(), "password",
                inputUser.role(),
                inputUser.employmentDate());
    }

}
