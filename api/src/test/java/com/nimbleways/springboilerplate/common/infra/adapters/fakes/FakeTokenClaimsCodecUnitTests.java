package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.features.authentication.domain.ports.TokenClaimsCodecPortContractTests;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.AccessToken;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;

@UnitTest
public class FakeTokenClaimsCodecUnitTests extends TokenClaimsCodecPortContractTests {

    private static final String TOKEN_WITH_ROLES_PLACEHOLDER = """
        {"rand":"8cf92381-9492-42a2-8605-caca41be9aa3","tokenClaims":
        {"userPrincipal":{"id":"54cd8ce9-14d3-4568-8a80-f909388b58bf",
        "username":{"value":"username"},"roles":%s},"creationTime":1708449075.514227600,
        "expirationTime":1708449076.514227600}}""";

    @Override
    protected FakeTokenClaimsCodec getInstance() {
        return new FakeTokenClaimsCodec(getTimeProvider());
    }

    @Override
    protected AccessToken getTokenWithoutRoleAttribute() {
        return new AccessToken(TOKEN_WITH_ROLES_PLACEHOLDER.formatted("null"));
    }

    @Override
    protected AccessToken getTokenWithInvalidRolesArrayClaim() {
        return new AccessToken(TOKEN_WITH_ROLES_PLACEHOLDER.formatted("[1, 2, 3]"));
    }

    @Override
    protected AccessToken getTokenWithInvalidRolesScalarClaim() {
        return new AccessToken(TOKEN_WITH_ROLES_PLACEHOLDER.formatted("10"));
    }
}
