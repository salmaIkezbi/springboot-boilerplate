package com.nimbleways.springboilerplate.features.purchases.domain.usecases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetPurchaseSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;



import static com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder.aNewPurchase;
import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class GetPurchaseUseCaseUnitTests {
    private final GetPurchaseSut sut = Instance.create(GetPurchaseSut.class);

    @Test
    void returns_purchase_By_user_in_repository() {
        // GIVEN
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .build();
        User user = sut.userRepository().create(aNewUser()
                .userData(userData)
                .build());

        Purchase purchase1 = sut.purchaseRepository().create(aNewPurchase().userId(user.id())
                .build());

        Purchase purchase2 = sut.purchaseRepository().create(aNewPurchase().userId(user.id())
                .build());

        // WHEN
        ImmutableList<Purchase> userPurchases = sut.getPurchase(user.id());

        // THEN

        assertThat(userPurchases).hasSize(2)
                .containsExactlyInAnyOrder(purchase1, purchase2);
    }


}
