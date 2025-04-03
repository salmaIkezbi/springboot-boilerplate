package com.nimbleways.springboilerplate.features.purchases.domain.usecases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetCoworkersPurchasesSut;
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
class GetCoworkersPurchasesUseCaseUnitTests {
    private final GetCoworkersPurchasesSut sut = Instance.create(GetCoworkersPurchasesSut.class);

    @Test
    void shouldFindPurchasesNotOwnedByGivenUserId() {
        // GIVEN
        NewUserFixture.UserData userData1 = new NewUserFixture.UserData.Builder()
                .build();
        User owner = sut.userRepository().create(aNewUser()
                .userData(userData1)
                .build());

        NewUserFixture.UserData userData2 = new NewUserFixture.UserData.Builder()
                .build();
        User notOwner = sut.userRepository().create(aNewUser()
                .userData(userData2)
                .build());

        Purchase purchase1 = sut.purchaseRepository().create(aNewPurchase().userId(owner.id())
                .build());

        sut.purchaseRepository().create(aNewPurchase().userId(notOwner.id())
                .build());


        // WHEN
        ImmutableList<Purchase> retrievedPurchases = sut.getCoworkersPurchases(notOwner.id());

        // THEN
        assertThat(retrievedPurchases).isNotEmpty();
        assertThat(retrievedPurchases).hasSize(1)
                .containsExactlyInAnyOrder(purchase1);

    }
}
