package com.nimbleways.springboilerplate.features.purchases.api.endpoints.getcoworkerspurchases;


import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.features.puchases.api.endpoints.getcoworkerspurchases.GetCoworkersPurchasesEndpoint;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetCoworkersPurchasesSut;
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

@WebMvcTest(controllers = GetCoworkersPurchasesEndpoint.class)
@Import(GetCoworkersPurchasesSut.class)
class GetCoworkersPurchasesEndpointIntegrationTests extends BaseWebMvcIntegrationTests {
    public static final String GET_COWORKERS_PURCHASE = "/purchases/coworkerspurchases/";

    @Autowired
    private GetCoworkersPurchasesSut getCoworkersPurchasesSut;

    @Test
    void getting_coworkerspurchases_successful() throws Exception {
        //GIVEN
        User user1 = createUserInRepo(String.valueOf(Role.USER));
        createPurchaseInRepo(user1.id(), "brand", "model", 0.0, "store", null);

        User user2 = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchaseUser2 = createPurchaseInRepo(user2.id(), "brand", "model", 0.0, "store", null);

        User user3 = createUserInRepo(String.valueOf(Role.USER));
        Purchase purchaseUser3 = createPurchaseInRepo(user3.id(), "brand", "model", 0.0, "store", null);

        String expectedJson = """
        [
            {
                "id": "%s",
                "userId": "%s",
                "brand": "brand",
                "model": "model",
                "store": "store",
                "price": 0.0,
                "pathImage": []
            },
            {
                "id": "%s",
                "userId": "%s",
                "brand": "brand",
                "model": "model",
                "store": "store",
                "price": 0.0,
                "pathImage": []
            }
        ]
    """.formatted(purchaseUser2.id(), user2.id(), purchaseUser3.id(), user3.id());

        // WHEN & THEN
        mockMvc
                .perform(get(GET_COWORKERS_PURCHASE + user1.id().toString())
                        .cookie(getAccessTokenCookie(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonIgnoreArrayOrder(expectedJson));

    }


    private User createUserInRepo(String role) {
        return getCoworkersPurchasesSut.userRepository().create(
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
        return getCoworkersPurchasesSut.purchaseRepository().create(
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
