package com.nimbleways.springboilerplate.common.api.exceptionhandling;

import static com.nimbleways.springboilerplate.testhelpers.utils.JsonUtils.jsonIgnoreArrayOrder;

import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseApplicationTestsWithoutDb;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Import({ExceptionHandlingFakeEndpoint.class})
class GlobalExceptionHandlerApplicationTests extends BaseApplicationTestsWithoutDb {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public JwtDecoder decoder() {
            return token -> {
                throw new FakeException();
            };
        }
    }

    @Test
    void returns_problemDetail_when_an_exception_is_thrown_during_authentication() throws JSONException {
        // WHEN
        webTestClient
                .get().uri(ExceptionHandlingFakeEndpoint.PERMIT_ALL_GET_ENDPOINT)
                .header(HttpHeaders.COOKIE, "accessToken=x")
                .exchange()

        // THEN
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody().json("""
                    {"type":"about:blank","title":"errors.internal_server_error","status":500,
                    "detail":"errors.internal_server_error","instance":"/api/exception-handling/get"}""",
                        jsonIgnoreArrayOrder);
    }
}
