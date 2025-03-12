package com.nimbleways.springboilerplate.testhelpers.baseclasses;

import com.nimbleways.springboilerplate.common.api.LoggingEventListener;
import com.nimbleways.springboilerplate.common.api.beans.ObjectMapperConfiguration;
import com.nimbleways.springboilerplate.common.api.security.StandaloneJwtAuthentication;
import com.nimbleways.springboilerplate.common.api.security.WebSecurityConfiguration;
import com.nimbleways.springboilerplate.common.domain.ports.TimeProviderPort;
import com.nimbleways.springboilerplate.common.infra.adapters.SpringEventPublisher;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakeTokenClaimsCodec;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.TokenClaims;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;
import com.nimbleways.springboilerplate.features.authentication.domain.valueobjects.AccessToken;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests.LoggerBean;
import com.nimbleways.springboilerplate.testhelpers.configurations.TimeTestConfiguration;
import jakarta.servlet.http.Cookie;
import java.time.Instant;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;
import static com.nimbleways.springboilerplate.testhelpers.helpers.TokenHelpers.urlEncode;
import static com.nimbleways.springboilerplate.testhelpers.utils.JsonUtils.jsonIgnoreArrayOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Import({WebSecurityConfiguration.class, StandaloneJwtAuthentication.class,
        FakeTokenClaimsCodec.class, TimeTestConfiguration.class, LoggingEventListener.class, LoggerBean.class,
        ObjectMapperConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseWebMvcIntegrationTests {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected TimeProviderPort timeProvider;
    @Autowired
    protected FakeTokenClaimsCodec fakeTokenClaimsCodec;

    protected BaseWebMvcIntegrationTests() {
    }

    protected Cookie getAccessTokenCookie(User user) {
        return getAccessTokenCookie(toUserPrincipal(user));
    }

    protected Cookie getAccessTokenCookie(UserPrincipal userPrincipal) {
        Instant instant = timeProvider.instant();
        TokenClaims tokenClaims = new TokenClaims(userPrincipal, instant, instant.plusSeconds(1));
        AccessToken accessToken = fakeTokenClaimsCodec.encode(tokenClaims);
        return new Cookie("accessToken", urlEncode(accessToken.value()));
    }

    protected ResultMatcher jsonStrictArrayOrder(String jsonContent) {
        return content().json(jsonContent, JsonCompareMode.STRICT);
    }

    protected ResultMatcher jsonIgnoreArrayOrder(String jsonContent) {
        return content().json(jsonContent, jsonIgnoreArrayOrder);
    }

    @TestConfiguration
    static class LoggerBean {
        @Bean
        public ILoggerFactory loggerFactory() {
            return LoggerFactory.getILoggerFactory();
        }

        @Bean
        @Primary
        public SpringEventPublisher springEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            return new SpringEventPublisher(applicationEventPublisher);
        }
    }
}
