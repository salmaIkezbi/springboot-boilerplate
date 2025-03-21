package com.nimbleways.springboilerplate.features.users.api.endpoints.getusers;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.GetUserSut;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@WebMvcTest(controllers = GetUserEndpoint.class)
@Import(GetUserSut.class)
class GetUserEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
        public static final String GET_USER_ENDPOINT = "/user/";

        @Autowired
        private GetUserSut getUserSut;

        @Test
        void getUser_whenSingleUserExistsInRepositoryAndHasNormalRole_returnsThatUser() throws Exception {
                // GIVEN
                User user = createUserInRepo("user", "user", String.valueOf(Role.USER));

                // WHEN & THEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT + user.id().toString())
                                                .cookie(getAccessTokenCookie(user)))
                                .andExpect(status().isOk())
                                .andExpect(jsonIgnoreArrayOrder("""
                                                {"id":"%s","email":"user","name":"user"}"""
                                                .formatted(user.id().toString())));
        }

        @Test
        void getting_users_with_non_admin_user_returns_200() throws Exception {
                // GIVEN
                User user = createUserInRepo("user1", "user1", String.valueOf(Role.USER));
                // WHEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT + user.id().toString())
                                                .cookie(getAccessTokenCookie(user)))

                                // THEN
                                .andExpect(status().isOk())
                                .andExpect(jsonIgnoreArrayOrder("""
                                                {"id":"%s","email":"user1","name":"user1"}"""
                                                .formatted(user.id().toString())));

        }

        @Test
        void getting_user_without_accessToken_returns_404() throws Exception {
                // GIVEN
                User user = createUserInRepo("user1", "email1", String.valueOf(Role.USER));

                // WHEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT, user.id())) // Passer l'ID de l'utilisateur dans l'URL
                                .andExpect(status().isNotFound()); // Retourne 401 pour non-authentifié
        }

        @Test
        void getting_user_without_accessToken_returns_401() throws Exception {
                // GIVEN
                User admin = createUserInRepo("admin", "admin", String.valueOf(Role.ADMIN));
                User user = createUserInRepo("user1", "email1", String.valueOf(Role.USER));

                // WHEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT + user.id().toString())
                                                .cookie(getAccessTokenCookie(admin)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getting_non_existent_user_returns_not_found() throws Exception {
                // GIVEN
                User user = createUserInRepo("user", "user", String.valueOf(Role.USER));
                UUID nonExistentUserId = UUID.randomUUID();

                // WHEN & THEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT + nonExistentUserId.toString())
                                                .cookie(getAccessTokenCookie(user)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getting_user_with_null_id_returns_not_found() throws Exception {
                // GIVEN
                User user = createUserInRepo("user", "user", String.valueOf(Role.USER));

                // WHEN & THEN
                mockMvc
                                .perform(get(GET_USER_ENDPOINT)
                                                .cookie(getAccessTokenCookie(user)))
                                .andExpect(status().isNotFound());
        }

        private User createUserInRepo(String name, String email, String role) {
                return getUserSut.userRepository().create(
                                aNewUser()
                                                .userData(new NewUserFixture.UserData.Builder().name(name).email(email)
                                                                .role(role).build())
                                                .build());
        }

}
