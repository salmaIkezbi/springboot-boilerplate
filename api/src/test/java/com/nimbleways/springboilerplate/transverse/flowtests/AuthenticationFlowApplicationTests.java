package com.nimbleways.springboilerplate.transverse.flowtests;

import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseApplicationTestsWithDb;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthenticationFlowApplicationTests extends BaseApplicationTestsWithDb {

    @Test
    void signup_login_getUsers_refreshToken() {

        // ----------- Sign up new ADMIN user ------------//
        webTestClient
                .post().uri("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"name":"Name", "email":"admin1",
                        "password":"password1", "role":"ADMIN","employmentDate":"2025-03-17"}""")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.email").isEqualTo("admin1");

        // ----------- login as ADMIN ------------//
        webTestClient
                .post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"email":"admin1", "password":"password1"}""")
                .exchange()
                .expectStatus().isOk();

        // ----------- Sign up a normal USER ------------//
        webTestClient
                .post().uri("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"name":"User", "email":"user1",
                        "password":"password1", "role":"USER","employmentDate":"2025-03-17"}""")
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.email").isEqualTo("user1");

        // ----------- login as USER ------------//
        webTestClient
                .post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"email":"user1", "password":"password1"}""")
                .exchange()
                .expectStatus().isOk();

        // ----------- refresh token ------------//
        webTestClient
                .post().uri("/auth/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        // ----------- logout ------------//
        webTestClient
                .post().uri("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}
