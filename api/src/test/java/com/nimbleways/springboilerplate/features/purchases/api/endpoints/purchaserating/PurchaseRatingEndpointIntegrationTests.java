package com.nimbleways.springboilerplate.features.purchases.api.endpoints.purchaserating;




import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.puchases.api.endpoints.purchaserating.PurchaseRatingEndpoint;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.PurchaseRatingSut;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = PurchaseRatingEndpoint.class)
@Import(PurchaseRatingSut.class)
class PurchaseRatingEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
    public static final String PUT_PURCHASE_ENDPOINT = "/purchases/";

    @Autowired
    private PurchaseRatingSut purchaseRatingSut;

    @Test
    void rating_purchase_with_valid_data_returns_200() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN & THEN
        mockMvc
                .perform(put(PUT_PURCHASE_ENDPOINT + purchase.id().toString())
                        .cookie(getAccessTokenCookie(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"rating":1}"""))
                .andExpect(status().isOk())
                .andExpect(jsonIgnoreArrayOrder("""
                    {"id":"%s","brand":"%s","model":"%s","price":0.0,"store":"%s","pathImage":[],"rate":1}"""
                        .formatted(purchase.id().toString(),purchase.brand(),purchase.model(),purchase.store())
                ));
    }

    @Test
    void rating_purchase_without_authentication_returns_401() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN & THEN
        mockMvc
                .perform(put(PUT_PURCHASE_ENDPOINT + purchase.id().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"rating":1}"""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rating_purchase_with_admin_role_returns_403() throws Exception {
        // GIVEN
        User admin = createUserInRepo(String.valueOf(Role.ADMIN));
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN & THEN
        mockMvc
                .perform(put(PUT_PURCHASE_ENDPOINT + purchase.id().toString())
                        .cookie(getAccessTokenCookie(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"rating":1}"""))
                .andExpect(status().isForbidden());
    }

    @Test
    void rating_purchase_with_non_existent_id_returns_404() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        UUID nonExistentPurchaseId = UUID.randomUUID();

        // WHEN & THEN
        mockMvc
                .perform(put(PUT_PURCHASE_ENDPOINT + nonExistentPurchaseId)
                        .cookie(getAccessTokenCookie(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"rating":7}"""))
                .andExpect(status().isNotFound());
    }

    @Test
    void rating_purchase_with_invalid_rating_value_returns_400() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);

        // WHEN & THEN
        mockMvc
                .perform(put(PUT_PURCHASE_ENDPOINT + purchase.id().toString())
                        .cookie(getAccessTokenCookie(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"rating":"good product"}"""))
                .andExpect(status().isBadRequest());
    }


    private User createUserInRepo(String role) {
        return purchaseRatingSut.userRepository().create(
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
        return purchaseRatingSut.purchaseRepository().create(
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
