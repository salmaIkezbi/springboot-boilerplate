package com.nimbleways.springboilerplate.features.users.domain.ports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nimbleways.springboilerplate.common.infra.adapters.TimeProvider;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserCredential;
import com.nimbleways.springboilerplate.features.authentication.domain.ports.UserCredentialsRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.EmailAlreadyExistsInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Transactional
public abstract class UserRepositoryPortContractTests {
    private UserRepositoryPort userRepository;
    private UserCredentialsRepositoryPort userCredentialsRepositoryPort;

    @BeforeEach
    public void createSut() {
        userRepository = getUserRepository();
        userCredentialsRepositoryPort = getUserCredentialsRepository();
    }

    @Test
    void creating_a_new_user_succeed() {
        NewUser newUser = aNewUser().build();
        userRepository.create(newUser);
        NewUser newUserFromDb = reconstructNewUserFromDb(newUser.email().value());
        assertEquals(newUser, newUserFromDb);
    }

    @Test
    void creating_a_new_user_returns_the_created_user() {
        NewUser newUser = aNewUser().build();

        User user = userRepository.create(newUser);

        assertEquals(List.of(user), userRepository.findAll());
    }

    @Test
    void creating_a_new_user_with_an_existing_email_throws_UserAlreadyExistsInRepositoryException() {
        NewUserFixture.UserData userData1 = new NewUserFixture.UserData.Builder()
                .email("email")
                .build();
        NewUserFixture.UserData userData2 = new NewUserFixture.UserData.Builder()
                .email("email")
                .build();
        NewUser firstNewUser = aNewUser().userData(userData1)
                .build();
        NewUser secondNewUser = aNewUser().userData(userData2)
                .build();
        userRepository.create(firstNewUser);

        Exception exception = assertThrows(Exception.class,
                () -> userRepository.create(secondNewUser));

        assertEquals(EmailAlreadyExistsInRepositoryException.class, exception.getClass());
        assertEquals("email 'email' already exist in repository", exception.getMessage());
    }

    private static NewUserBuilder aNewUser() {
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .timeProvider(TimeProvider.UTC)
                .build();
        return NewUserBuilder.aNewUser().userData(userData);
    }

    private NewUser reconstructNewUserFromDb(String email) {
        User user = userRepository.findAll().detectOptional(u -> u.email().value().equals(email)).orElseThrow();
        UserCredential userCredential = userCredentialsRepositoryPort
                .findUserCredentialByEmail(email).orElseThrow();
        return new NewUser(
                user.name(),
                user.email(),
                userCredential.encodedPassword(),
                user.createdAt(),
                user.role(),
                user.employmentDate());
    }

    // --------------------------------- Protected Methods
    // ------------------------------- //
    protected abstract UserRepositoryPort getUserRepository();

    protected abstract UserCredentialsRepositoryPort getUserCredentialsRepository();
}
