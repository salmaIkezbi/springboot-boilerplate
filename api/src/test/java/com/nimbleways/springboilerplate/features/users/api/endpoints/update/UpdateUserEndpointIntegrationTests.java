package com.nimbleways.springboilerplate.features.users.api.endpoints.update;


import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.nimbleways.springboilerplate.features.users.api.endpoints.updateuser.UpdateEndpoint;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests;

import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import com.nimbleways.springboilerplate.features.users.domain.usecases.suts.UpdateSut;


@WebMvcTest(controllers = UpdateEndpoint.class)
@Import(UpdateSut.class)
class UpdateUserEndpointIntegrationTests extends BaseWebMvcIntegrationTests{
    public static final String UPDATE_ENDPOINT = "/users/";

    @Autowired
    private UpdateSut updateSut;


    @Test
    void update_returns_user_with_200_status() throws Exception {
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .email("emailCreated")
                .plainPassword("passwordCreated")
                .build();
        User user = updateSut.userRepository().create(
                aNewUser()
                        .userData(userData)
                        .build());
        // WHEN
        mockMvc
            .perform(put(UPDATE_ENDPOINT + user.id().toString())
                    .cookie(getAccessTokenCookie(user))
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "password": "password",
                        "shouldReceiveMailNotifications": false,
                        "shouldReceiveApprovalNotifications": true
                    }
                """)
                )
                // THEN
                .andExpect(status().isCreated())
                .andExpect(jsonIgnoreArrayOrder("""
            {
                "id": "%s",
                "name": "%s",
                "email": "%s",
                "role": "%s",
                "employmentDate": "%s",
                "shouldReceiveMailNotifications": false,
                "shouldReceiveApprovalNotifications": true
            }
            """.formatted(
                                user.id().toString(),
                                user.name(),
                                user.email().value(),
                                user.role(),
                                user.employmentDate().toString()
                        )
                ));
    }
}
