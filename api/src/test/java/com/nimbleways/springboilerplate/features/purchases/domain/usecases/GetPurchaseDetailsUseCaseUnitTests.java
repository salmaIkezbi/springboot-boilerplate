package com.nimbleways.springboilerplate.features.purchases.domain.usecases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetDetailsSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.junit.jupiter.api.Test;

import static com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder.aNewPurchase;
import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class GetPurchaseDetailsUseCaseUnitTests {
    private final GetDetailsSut sut = Instance.create(GetDetailsSut.class);

    @Test
    void returns_purchase_By_user_in_repository() {
        // GIVEN
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        User user = sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());

        Purchase purchase = sut.purchaseRepository().create(aNewPurchase()
                .userId(user.id())
                .build());

        // WHEN
        Purchase purchaseRetrieved = sut.getDetails(purchase.id());

        // THEN
        assertThat(purchaseRetrieved).isNotNull();
        assertThat(purchaseRetrieved.id()).isEqualTo(purchase.id());
        assertThat(purchaseRetrieved.userId()).isEqualTo(user.id());
    }

}
