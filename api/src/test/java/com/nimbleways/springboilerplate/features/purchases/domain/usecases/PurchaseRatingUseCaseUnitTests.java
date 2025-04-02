package com.nimbleways.springboilerplate.features.purchases.domain.usecases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.PurchaseRating;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.PurchaseRatingSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.junit.jupiter.api.Test;

import static com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder.aNewPurchase;
import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class PurchaseRatingUseCaseUnitTests {
    private final PurchaseRatingSut sut = Instance.create(PurchaseRatingSut.class);

    @Test
    void returns_purchase_By_user_in_repository() {
        // GIVEN
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        User user = sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());

        Purchase purchase = sut.purchaseRepository().create(aNewPurchase().userId(user.id())
                .build());

        PurchaseRating purchaseRating = new PurchaseRating(
                purchase.id(),
                10
        );

        // WHEN
        Purchase updatedPurchase = sut.rate(purchaseRating);

        // THEN
        assertThat(updatedPurchase).isNotNull();
        assertThat(updatedPurchase.id()).isEqualTo(purchase.id());
        assertThat(updatedPurchase.rate()).isEqualTo(10);


    }

}
