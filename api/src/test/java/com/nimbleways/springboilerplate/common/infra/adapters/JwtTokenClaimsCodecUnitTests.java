package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.properties.JwtProperties;
import com.nimbleways.springboilerplate.features.authentication.domain.ports.TokenClaimsCodecPortContractTests;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.AccessToken;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeRandomGenerator;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Timeout;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UnitTest
@Timeout(value = 3, unit = TimeUnit.SECONDS) // JWT cryptography is slow in CI
public class JwtTokenClaimsCodecUnitTests extends TokenClaimsCodecPortContractTests {

    private static final String ISSUER = "myapp";
    private static final String SIGNING_KEY_STRING = "zdtlD3JK56m6wTTgsNFhqzjqPaaaddingFor256bits=";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGNING_KEY_STRING));

    @Override
    protected JwtTokenClaimsCodec getInstance() {
        return new JwtTokenClaimsCodec(
            new JwtProperties(SIGNING_KEY_STRING, ISSUER),
            new FakeRandomGenerator(),
            getTimeProvider());
    }

    @Override
    protected AccessToken getTokenWithoutRoleAttribute() {
        String jwt = getJwtBuilder().compact();
        return new AccessToken(jwt);
    }

    @Override
    protected AccessToken getTokenWithInvalidRolesArrayClaim() {
        String jwt = getJwtBuilder()
                .claim("scope", new int[] {1, 2, 3}).compact();
        return new AccessToken(jwt);
    }

    @Override
    protected AccessToken getTokenWithInvalidRolesScalarClaim() {
        String jwt = getJwtBuilder()
                .claim("scope", 10).compact();
        return new AccessToken(jwt);
    }

    @Override
    protected Instant adjustPrecision(Instant instant) {
        return instant.truncatedTo(ChronoUnit.SECONDS);
    }

    private JwtBuilder getJwtBuilder() {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .signWith(SIGNING_KEY)
                .issuedAt(Date.from(getTimeProvider().instant()))
                .expiration(Date.from(getTimeProvider().instant().plusSeconds(1)));
    }
}
