package com.nimbleways.springboilerplate.common.infra.adapters;

import static com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipalBuilder.aUserPrincipal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nimbleways.springboilerplate.common.domain.ports.TimeProviderPort;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.TokenClaims;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;
import com.nimbleways.springboilerplate.testhelpers.configurations.TimeTestConfiguration;

import java.time.Instant;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;

public abstract class SpringJwtDecoderContractTests {
    private TimeProviderPort timeProvider;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    public void createSut() {
        timeProvider = getTimeProvider();
        jwtDecoder = getInstance();
    }

    @Test
    void returns_valid_Jwt_with_one_role() {
        TokenClaims claims = getTokenClaims(timeProvider.instant().plusSeconds(10), String.valueOf(Role.USER));
        String token = encode(claims);

        Jwt jwt = jwtDecoder.decode(token);

        assertEquals(adjustPrecision(claims.expirationTime()), jwt.getExpiresAt());
        assertEquals(adjustPrecision(claims.creationTime()), jwt.getIssuedAt());
        assertEquals("USER", jwt.getClaim("scope"));
        assertEquals(getExpectedSubject(claims.userPrincipal()), jwt.getSubject());
    }


    @Test
    void throws_JwtValidationException_if_expired() {
        TokenClaims claims = getTokenClaims(timeProvider.instant().minusSeconds(1), String.valueOf(Role.USER));
        String token = encode(claims);

        JwtValidationException exception = assertThrows(JwtValidationException.class, () -> jwtDecoder.decode(token));

        assertEquals(List.of(OAuth2Error.class), exception.getErrors().stream().map(OAuth2Error::getClass).toList());
    }

    @Test
    void throws_BadJwtException_if_malformed() {
        String token = getMalformedToken();

        Exception exception = assertThrows(Exception.class, () -> jwtDecoder.decode(token));

        assertEquals(BadJwtException.class, exception.getClass());
    }

    private String getExpectedSubject(UserPrincipal userPrincipal) {
        return "%s,%s".formatted(userPrincipal.id(), userPrincipal.email().value());
    }

    @NotNull
    private TokenClaims getTokenClaims(Instant expirationTime,String role) {
        return new TokenClaims(
                aUserPrincipal().role(role).build(),
                expirationTime.minusSeconds(1),
                expirationTime);
    }

    // --------------------------------- Protected Methods
    // ------------------------------- //
    protected abstract JwtDecoder getInstance();

    protected abstract String encode(TokenClaims claims);

    protected abstract String getMalformedToken();

    protected Instant adjustPrecision(Instant instant) {
        return instant;
    }

    @NotNull
    protected TimeProviderPort getTimeProvider() {
        return TimeTestConfiguration.fixedTimeProvider();
    }

}
