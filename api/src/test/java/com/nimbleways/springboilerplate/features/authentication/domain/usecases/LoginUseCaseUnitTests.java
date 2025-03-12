package com.nimbleways.springboilerplate.features.authentication.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static com.nimbleways.springboilerplate.testhelpers.CustomAssertions.assertPresent;
import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;
import static com.nimbleways.springboilerplate.testhelpers.fixtures.SimpleFixture.aLoginCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nimbleways.springboilerplate.common.domain.events.Event;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.TokenClaims;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.events.UserLoggedInEvent;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.BadUserCredentialException;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.UnknownUsernameException;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.login.LoginCommand;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.RefreshToken;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.UserTokens;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.suts.LoginSut;
import java.time.Instant;
import java.util.Optional;

import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;

@UnitTest
class LoginUseCaseUnitTests {

    private final LoginSut sut = Instance.create(LoginSut.class);

    @Test
    void login_with_good_username_and_password_returns_user_tokens() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand();
        User user = sut.userRepository().create(getUser(loginCommand));

        UserPrincipal expectedUserPrincipal = toUserPrincipal(user);
        Instant expectedExpirationTime = sut.timeProvider().instant().plus(sut.tokenProperties().accessTokenValidityDuration());

        // WHEN
        UserTokens tokens = sut.login(loginCommand);

        // THEN
        TokenClaims claims = getTokenClaims(tokens);
        assertEquals(expectedUserPrincipal, claims.userPrincipal());
        assertEquals(expectedExpirationTime, claims.expirationTime());
    }

    @Test
    void login_with_good_username_and_password_returns_new_tokens_each_time() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand();
        sut.userRepository().create(getUser(loginCommand));

        // WHEN
        UserTokens firstTokens = sut.login(loginCommand);
        UserTokens secondTokens = sut.login(loginCommand);

        // THEN
        assertNotEquals(firstTokens.accessToken(), secondTokens.accessToken());
        assertNotEquals(firstTokens.refreshToken(), secondTokens.refreshToken());
    }

    @Test
    void login_with_good_username_and_password_publishes_UserLoggedInEvent() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand();
        User user = sut.userRepository().create(getUser(loginCommand));

        UserPrincipal expectedUserPrincipal = toUserPrincipal(user);

        // WHEN
        sut.login(loginCommand);

        // THEN
        UserLoggedInEvent userLoggedInEvent = assertEventPublished(UserLoggedInEvent.class);
        assertEquals(expectedUserPrincipal, userLoggedInEvent.userPrincipal());
        assertNotNull(userLoggedInEvent.sourceType());
        assertEquals("Login attempt successful for user with id: " + user.id(), userLoggedInEvent.toString());
    }

    @Test
    void login_with_good_username_and_password_returns_a_refreshToken_with_expected_duration() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand();
        User user = sut.userRepository().create(getUser(loginCommand));

        UserSession expectedUserSession = new UserSession(
                new RefreshToken("cfcd2084-95d5-35ef-a6e7-dff9f98764da"),
                sut.timeProvider().instant().plus(sut.tokenProperties().refreshTokenValidityDuration()),
                toUserPrincipal(user)
        );

        // WHEN
        sut.login(loginCommand);

        // THEN
        ImmutableList<UserSession> userSessions = sut.userSessionRepository().findAll();
        assertThat(userSessions).containsExactly(expectedUserSession);
    }

    @Test
    void login_with_non_existing_username_throws_UnknownUsernameException() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand();

        // WHEN
        Exception ex = assertThrows(Exception.class, () -> sut.login(loginCommand));

        // THEN
        assertEquals(UnknownUsernameException.class, ex.getClass());
        assertEquals("Username not found: username", ex.getMessage());
    }

    @Test
    void login_with_good_username_and_bad_password_throws_BadUserCredentialException() {
        // GIVEN
        LoginCommand loginCommand = aLoginCommand("bad_password");
        sut.userRepository().create(getUser(loginCommand, "password"));

        // WHEN
        Exception ex = assertThrows(Exception.class, () -> sut.login(loginCommand));

        // THEN
        assertEquals(BadUserCredentialException.class, ex.getClass());
        assertEquals("Bad password provided for username: username", ex.getMessage());
    }

    private TokenClaims getTokenClaims(UserTokens tokens) {
        return sut.tokenGenerator().decodeWithoutExpirationValidation(tokens.accessToken());
    }

    private static NewUser getUser(LoginCommand loginCommand, String password) {
        return aNewUser()
            .username(loginCommand.username().value())
            .plainPassword(password)
            .build();
    }

    private static NewUser getUser(LoginCommand loginCommand) {
        return getUser(loginCommand, loginCommand.password());
    }

    private <T extends Event> T assertEventPublished(Class<T> eventType) {
        Optional<T> lastEvent = sut.eventPublisher().lastEvent(eventType);
        return assertPresent(lastEvent);
    }

}
