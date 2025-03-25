package com.nimbleways.springboilerplate.features.authentication.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.RefreshAndAccessTokensMismatchException;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.RefreshTokenExpiredOrNotFoundException;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.recreateusertokens.RecreateUserTokensCommand;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.UserTokens;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.suts.RecreateUserTokensSut;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.junit.jupiter.api.Test;

@UnitTest
class RecreateUserTokensUseCaseUnitTests {

    private final RecreateUserTokensSut sut = Instance.create(RecreateUserTokensSut.class);

    @Test
    void recreating_tokens_returns_new_user_tokens_and_update_user_sessions() {
        // GIVEN
        UserTokens oldUserTokens = sut.addUserAndSessionToRepository().userTokens();

        // WHEN
        UserTokens newUserTokens = sut.recreateUserTokens(new RecreateUserTokensCommand(oldUserTokens));


        // THEN
        assertNotEquals(oldUserTokens.accessToken(), newUserTokens.accessToken());
        assertNotEquals(oldUserTokens.refreshToken(), newUserTokens.refreshToken());
        assertEquals(sut.tokenGenerator().lastCreatedToken(), newUserTokens.accessToken());
        assertThat(sut.userSessionRepository().findAll())
                .hasSize(1)
                .allMatch(s -> s.refreshToken().equals(newUserTokens.refreshToken()));
    }

    @Test
    void recreating_tokens_with_non_existing_refreshToken_throws_RefreshTokenExpiredOrNotFoundException() {
        // GIVEN
        UserTokens userTokens = createUserTokens();

        // WHEN
        Exception ex = assertThrows(Exception.class,
                () -> sut.recreateUserTokens(new RecreateUserTokensCommand(userTokens)));

        //THEN
        assertEquals(RefreshTokenExpiredOrNotFoundException.class, ex.getClass());
        assertEquals("RefreshToken has expired or not present in database: " + userTokens.refreshToken().value(), ex.getMessage());
    }

    @Test
    void recreating_tokens_with_accessToken_and_refreshToken_belonging_to_different_users_throws_RefreshAndAccessTokensMismatchException() {
        // GIVEN
        NewUserFixture.UserData userData1 = new NewUserFixture.UserData.Builder()
                .build();
        User user1 = sut.userRepository().create(aNewUser()
                .userData(userData1)
                .build());
        NewUserFixture.UserData userData2 = new NewUserFixture.UserData.Builder()
                .build();
        User user2 = sut.userRepository().create(aNewUser()
                .userData(userData2)
                .build());
        UserTokens user1Tokens = sut.createUserSessionAndTokens(user1).userTokens();
        UserSession sessionWithUser1RefreshTokenAndUser2Principal = new UserSession(
            user1Tokens.refreshToken(),
            sut.timeProvider().instant().plusSeconds(1),
            toUserPrincipal(user2));
        sut.userSessionRepository().create(sessionWithUser1RefreshTokenAndUser2Principal);

        // WHEN
        Exception ex = assertThrows(
            Exception.class,
            () -> sut.recreateUserTokens(new RecreateUserTokensCommand(user1Tokens)));

        //THEN
        assertEquals(RefreshAndAccessTokensMismatchException.class, ex.getClass());
        assertEquals("User ids from RefreshToken and AccessToken don't match. RefreshToken userId: %s | AccessToken userId: %s".formatted(
            user2.id(), user1.id()),
            ex.getMessage());
    }

    private UserTokens createUserTokens() {
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        User user = sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());
        return sut.createUserSessionAndTokens(user).userTokens();
    }
}
