package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.common.infra.adapters.SpringJwtDecoderContractTests;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.TokenClaims;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@UnitTest
public final class FakeTokenClaimsCodecAsJwtDecoderUnitTests extends SpringJwtDecoderContractTests {
    private final FakeTokenClaimsCodec fakeTokenClaimsCodec;

    public FakeTokenClaimsCodecAsJwtDecoderUnitTests() {
        super();
        fakeTokenClaimsCodec = new FakeTokenClaimsCodec(getTimeProvider());
    }

    @Override
    protected JwtDecoder getInstance() {
        return fakeTokenClaimsCodec;
    }

    @Override
    protected String encode(TokenClaims claims) {
        return fakeTokenClaimsCodec.encode(claims).value();
    }

    @Override
    protected String getMalformedToken() {
        return "invalidToken";
    }
}
