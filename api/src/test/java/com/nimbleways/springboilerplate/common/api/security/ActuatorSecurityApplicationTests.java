package com.nimbleways.springboilerplate.common.api.security;

import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseApplicationTestsWithoutDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;

import static com.nimbleways.springboilerplate.testhelpers.utils.JsonUtils.jsonIgnoreArrayOrder;

// This is an ApplicationTests because I didn't manage to make mockmvc return a problemdetails when access denied
class ActuatorSecurityApplicationTests extends BaseApplicationTestsWithoutDb {

    @Test
    void get_on_actuator_base_path_with_good_email_and_password_returns_200(
            @Value("${management.server.user}") String email,
            @Value("${management.server.password}") String password) {
        // WHEN
        webTestClient
                .get().uri("/actuatorz")
                .header("Authorization", baseAuthHeaderValue(email, password))
                .exchange()

        // THEN
                .expectStatus().isOk()
                .expectBody().json("""
                        {"_links":{"self":{"href":"%s/actuatorz","templated":false},
                        "info":{"href":"%s/actuatorz/info","templated":false}}}"""
                        .formatted(baseUrl, baseUrl), jsonIgnoreArrayOrder);
    }

    @Test
    void get_on_actuator_base_path_with_good_email_and_bad_password_returns_401_and_WWW_Authenticate_header(
            @Value("${management.server.user}") String email
    ) {
        // WHEN
        webTestClient
                .get().uri("/actuatorz")
                .header("Authorization", baseAuthHeaderValue(email, "bad_password"))
                .exchange()

                // THEN
                .expectStatus().isUnauthorized()
                .expectHeader().valueEquals("www-authenticate", "Basic realm=\"Actuator\"")
                .expectBody().json("""
                        {"type" : "about:blank", "title" : "errors.unauthorized", "status" : 401,
                         "detail" : "errors.unauthorized", "instance" : "%s/actuatorz"}"""
                        .formatted(contextPath), jsonIgnoreArrayOrder);
    }

    @Test
    void get_on_actuator_base_path_with_bad_email_and_password_returns_401_and_WWW_Authenticate_header() {
        // WHEN
        webTestClient
                .get().uri("/actuatorz")
                .header("Authorization", baseAuthHeaderValue("bad_user", "bad_password"))
                .exchange()

        // THEN
                .expectStatus().isUnauthorized()
                .expectHeader().valueEquals("www-authenticate", "Basic realm=\"Actuator\"")
                .expectBody().json("""
                        {"type" : "about:blank", "title" : "errors.unauthorized", "status" : 401,
                         "detail" : "errors.unauthorized", "instance" : "%s/actuatorz"}"""
                        .formatted(contextPath), jsonIgnoreArrayOrder);
    }

    @Test
    void get_on_actuator_base_path_without_email_and_password_returns_401_and_WWW_Authenticate_header() {
        // WHEN
        webTestClient
                .get().uri("/actuatorz")
                .exchange()

        // THEN
                .expectStatus().isUnauthorized()
                .expectHeader().valueEquals("www-authenticate", "Basic realm=\"Actuator\"")
                .expectBody().json("""
                        {"type" : "about:blank", "title" : "errors.unauthorized", "status" : 401,
                         "detail" : "errors.unauthorized", "instance" : "%s/actuatorz"}"""
                        .formatted(contextPath), jsonIgnoreArrayOrder);
    }

    private static String baseAuthHeaderValue(String email, String password) {
        String credentials = email + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
