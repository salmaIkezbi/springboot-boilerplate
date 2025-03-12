package com.nimbleways.springboilerplate.features.authentication.domain.usecases;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;
import static com.nimbleways.springboilerplate.testhelpers.fixtures.SimpleFixture.aRefreshToken;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.usecases.suts.PurgeRefreshTokensSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import java.time.Instant;
import java.util.List;

import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.junit.jupiter.api.Test;

@UnitTest
class PurgeRefreshTokensUseCaseUnitTests {

    private final PurgeRefreshTokensSut sut = Instance.create(PurgeRefreshTokensSut.class);

    @Test
    void purging_deletes_all_expired_refreshTokens() {
        // GIVEN
        User user = createUser();
        Instant now = sut.timeProvider().instant();
        UserSession nonExpiredUserSession = addSession(user, now.plusMillis(1));
        addSession(user, now.minusMillis(1));
        addSession(user, now.minusMillis(2));

        // WHEN
        sut.purgeRefreshToken();

        //ASSERT
        assertEquals(List.of(nonExpiredUserSession), sut.userSessionRepository().findAll());
    }

    private User createUser() {
        return sut.userRepository().create(aNewUser().build());
    }

    private UserSession addSession(User user, Instant expirationDate) {
        UserSession userSession = new UserSession(
            aRefreshToken(),
            expirationDate,
            toUserPrincipal(user)
        );
        sut.userSessionRepository().create(userSession);
        return userSession;
    }
}