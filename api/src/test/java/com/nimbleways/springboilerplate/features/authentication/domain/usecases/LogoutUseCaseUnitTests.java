package com.nimbleways.springboilerplate.features.authentication.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;
import static com.nimbleways.springboilerplate.testhelpers.fixtures.SimpleFixture.aRefreshToken;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.exceptions.RefreshTokenExpiredOrNotFoundException;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.suts.LogoutSut;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

@UnitTest
class LogoutUseCaseUnitTests {

    private final LogoutSut sut = Instance.create(LogoutSut.class);

    @Test
    void logout_with_existing_refreshToken_deletes_session_from_repository() {
        // GIVEN
        UserSession userSession1 = createUserSession();
        UserSession userSession2 = createUserSession();
        sut.userSessionRepository().create(userSession1);
        sut.userSessionRepository().create(userSession2);

        // WHEN
        sut.logout(userSession1.refreshToken());

        //THEN
        assertEquals(List.of(userSession2), sut.userSessionRepository().findAll());
    }

    @Test
    void logout_with_non_existing_refreshToken_throws_RefreshTokenExpiredOrNotFoundException() {
        // GIVEN
        UserSession userSession = createUserSession();

        // WHEN
        Exception ex = assertThrows(Exception.class, () -> sut.logout(userSession.refreshToken()));

        // THEN
        assertEquals(RefreshTokenExpiredOrNotFoundException.class, ex.getClass());
        assertEquals("RefreshToken has expired or not present in database: " + userSession.refreshToken().value()
                , ex.getMessage());
    }

    @NotNull
    private UserSession createUserSession() {
        User user = sut.userRepository().create(aNewUser().build());
        return new UserSession(
            aRefreshToken(),
            sut.timeProvider().instant().plusSeconds(5),
            toUserPrincipal(user)
        );
    }

}
