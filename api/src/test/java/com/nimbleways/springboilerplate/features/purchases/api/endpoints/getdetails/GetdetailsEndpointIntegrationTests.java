package com.nimbleways.springboilerplate.features.purchases.api.endpoints.getdetails;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.puchases.api.endpoints.getdetails.GetDetailsEndpoint;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetDetailsSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.baseclasses.BaseWebMvcIntegrationTests;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder.aNewPurchase;
import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = GetDetailsEndpoint.class)
@Import(GetDetailsSut.class)
class GetdetailsEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
    public static final String GET_PURCHASE_ENDPOINT = "/purchase/";

    @Autowired
    private GetDetailsSut getDetailsSut;

    @Test
    void getting_purchase_details_with_normal_user_and_a_single_purchase_in_repository_returns_that_purchase() throws Exception {
        // GIVEN
        User user = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchase = createPurchaseInRepo(user.id(), "brand", "model", 0.0, "store", null);
        // WHEN & THEN
        mockMvc
                .perform(get(GET_PURCHASE_ENDPOINT + purchase.id().toString())
                        .cookie(getAccessTokenCookie(user)))
                .andExpect(status().isOk())
                .andExpect(jsonIgnoreArrayOrder("""
                        {"id":%s,"brand":%s,"model":%s,"store":%s,"price":%s,"pathImage":%s}
                        """.formatted(purchase.id().toString(),purchase.brand(),purchase.model(),purchase.store(),purchase.price(),purchase.pathImage())));
    }


    private User createUserInRepo(String role) {
        return getDetailsSut.userRepository().create(
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
        return getDetailsSut.purchaseRepository().create(
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
