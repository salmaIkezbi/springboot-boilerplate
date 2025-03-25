package com.nimbleways.springboilerplate.features.users.api.endpoints.getusers;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.GetUsersSut;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@WebMvcTest(controllers = GetUsersEndpoint.class)
@Import(GetUsersSut.class)
class GetUsersEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
        public static final String GET_USERS_ENDPOINT = "/users";

        @Autowired
        private GetUsersSut getUsersSut;

        @Test
        void getting_users_with_admin_user_and_a_single_user_in_repository_returns_that_user() throws Exception {
                // GIVEN
                User admin = createUserInRepo("admin", "admin", String.valueOf(Role.ADMIN));

                // WHEN
                mockMvc
                                .perform(get(GET_USERS_ENDPOINT)
                                                .cookie(getAccessTokenCookie(admin)))

                                // THEN
                                .andExpect(status().isOk())
                                .andExpect(jsonIgnoreArrayOrder("""
                                                [{"id":"%s","email":"admin","name":"admin"}]"""
                                                .formatted(admin.id().toString())));
        }

        @Test
        void getting_users_with_non_admin_user_returns_403() throws Exception {
                // GIVEN
                User user = createUserInRepo("user1", "user1", String.valueOf(Role.USER));

                // WHEN
                mockMvc
                                .perform(get(GET_USERS_ENDPOINT)
                                                .cookie(getAccessTokenCookie(user)))

                                // THEN
                                .andExpect(status().isForbidden())
                                .andExpect(jsonIgnoreArrayOrder("""
                                                {"type":"about:blank","title":"errors.access_denied","status":403,
                                                "detail":"errors.access_denied","instance":"/users"}"""));
        }

        @Test
        void getting_users_with_admin_user_and_two_users_in_repository_returns_both_users() throws Exception {
                // GIVEN
                User user1 = createUserInRepo("user1", "email1", String.valueOf(Role.ADMIN));
                User user2 = createUserInRepo("user2", "email2", String.valueOf(Role.USER));

                // WHEN
                mockMvc
                                .perform(get(GET_USERS_ENDPOINT)
                                                .cookie(getAccessTokenCookie(user1)))

                                // THEN
                                .andExpect(status().isOk())
                                .andExpect(jsonIgnoreArrayOrder("""
                                                [{"id":"%s","email":"email1","name":"user1"},
                                                {"id":"%s","email":"email2","name":"user2"}]"""
                                                .formatted(user1.id().toString(), user2.id().toString())));
        }

        @Test
        void getting_users_without_accessToken_returns_401() throws Exception {
                // WHEN
                mockMvc
                                .perform(get(GET_USERS_ENDPOINT))

                                // THEN
                                .andExpect(status().isUnauthorized());
        }

    private User createUserInRepo(String name, String email, String role) {
        return getUsersSut.userRepository().create(
                aNewUser()
                        .userData(new NewUserFixture.UserData.Builder().name(name).email(email).role(role).build())
                        .build());
    }

}
