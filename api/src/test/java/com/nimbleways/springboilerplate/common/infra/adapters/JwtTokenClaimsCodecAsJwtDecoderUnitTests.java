package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeRandomGenerator;
import com.nimbleways.springboilerplate.common.infra.properties.JwtProperties;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.TokenClaims;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import org.junit.jupiter.api.Timeout;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;

@UnitTest
@Timeout(value = 3, unit = TimeUnit.SECONDS) // JWT cryptography is slow in CI
public final class JwtTokenClaimsCodecAsJwtDecoderUnitTests extends SpringJwtDecoderContractTests {
    private final JwtTokenClaimsCodec jwtTokenClaimsCodec;

    public JwtTokenClaimsCodecAsJwtDecoderUnitTests() {
        super();
        jwtTokenClaimsCodec = new JwtTokenClaimsCodec(
                new JwtProperties("zdtlD3JK56m6wTTgsNFhqzjqPaaaddingFor256bits=", "myapp"),
                new FakeRandomGenerator(),
                getTimeProvider());
    }

    @Override
    protected JwtDecoder getInstance() {
        return jwtTokenClaimsCodec;
    }

    @Override
    protected String encode(TokenClaims claims) {
        return jwtTokenClaimsCodec.encode(claims).value();
    }

    @Override
    protected String getMalformedToken() {
        return "invalidToken";
    }

    @Override
    // JWT only support time up to the seconds
    protected Instant adjustPrecision(Instant instant) {
        return instant.with(ChronoField.MILLI_OF_SECOND, 0);
    }
}
