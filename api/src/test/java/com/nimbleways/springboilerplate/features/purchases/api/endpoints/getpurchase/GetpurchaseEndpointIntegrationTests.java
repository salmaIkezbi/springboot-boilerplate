package com.nimbleways.springboilerplate.features.purchases.api.endpoints.getpurchase;


import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.puchases.api.endpoints.getpurchase.GetPurchaseEndpoint;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetPurchaseSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.eclipse.collections.api.list.ImmutableList;

import static com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder.aNewPurchase;
import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GetPurchaseEndpoint.class)
@Import(GetPurchaseSut.class)
class GetpurchaseEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
    public static final String GET_PURCHASE_ENDPOINT = "/purchase/";

    @Autowired
    private GetPurchaseSut getPurchaseSut;


    @Test
    void getting_purchase_with_normal_user_and_a_single_user_in_repository_returns_that_user() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN & THEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT + user.id().toString())
                        .cookie(getAccessTokenCookie(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(purchase.id().toString()))
                .andExpect(jsonPath("$.[0].userId").value(user.id().toString()))
                .andExpect(jsonPath("$.[0].brand").value("brand"))
                .andExpect(jsonPath("$.[0].model").value("model"))
                .andExpect(jsonPath("$.[0].store").value("store"))
                .andExpect(jsonPath("$.[0].price").value(0.0))
                .andExpect(jsonPath("$.[0].pathImage").isEmpty());
    }

    @Test
    void getting_purchases_with_non_admin_user_returns_200() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT + user.id().toString())
                        .cookie(getAccessTokenCookie(user)))

                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(purchase.id().toString()))
                .andExpect(jsonPath("$.[0].userId").value(user.id().toString()))
                .andExpect(jsonPath("$.[0].brand").value("brand"))
                .andExpect(jsonPath("$.[0].model").value("model"))
                .andExpect(jsonPath("$.[0].store").value("store"))
                .andExpect(jsonPath("$.[0].price").value(0.0))
                .andExpect(jsonPath("$.[0].pathImage").isEmpty());

    }
    @Test
    void getting_purchase_without_accessToken_returns_404() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));

        // WHEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT, user.id())) // Passer l'ID de l'utilisateur dans l'URL
                .andExpect(status().isNotFound()); // Retourne 401 pour non-authentifié
    }

    @Test
    void getting_user_without_accessRole_returns_401() throws Exception {
        // GIVEN
        User admin = createUserInRepo(String.valueOf(Role.ADMIN));
        User user = createUserInRepo(String.valueOf(Role.USER));

        // WHEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT + user.id().toString())
                        .cookie(getAccessTokenCookie(admin)))
                .andExpect(status().isForbidden()); // Retourne 401 pour non-authentifié
    }

    @Test
    void getting_purchase_for_non_existent_user_returns_not_found() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        UUID nonExistentUserId = UUID.randomUUID();


        // WHEN & THEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT + nonExistentUserId)
                        .cookie(getAccessTokenCookie(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getting_purchases_for_user_with_null_id_returns_not_found() throws Exception {
        // GIVEN
        User user = createUserInRepo( String.valueOf(Role.USER));

        // WHEN & THEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT )
                        .cookie(getAccessTokenCookie(user)))
                .andExpect(status().isNotFound()); // Supposant que votre contrôleur renvoie 404 quand l'utilisateur n'est pas trouvé
    }



    private User createUserInRepo(String role) {
        return getPurchaseSut.userRepository().create(
                aNewUser()
                        .userData(new NewUserFixture.UserData.Builder().role(role).build())
                        .build());
    }

    private Purchase createPurchaseInRepo(UUID userId,
                                          String brand,
                                          String model,
                                          Double price,
                                          String store,
                                          ImmutableList<String> pathImage) {
        return getPurchaseSut.purchaseRepository().create(
                aNewPurchase()
                        .userId(userId)
                        .brand(brand)
                        .model(model)
                        .price(price)
                        .store(store)
                        .pathImage(pathImage)
                        .build());
    }
    
}
