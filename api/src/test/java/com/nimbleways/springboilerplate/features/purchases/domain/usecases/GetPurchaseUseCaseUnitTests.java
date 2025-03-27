package com.nimbleways.springboilerplate.features.purchases.domain.usecases;

import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.purchases.domain.usecases.suts.GetPurchaseSut;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder.aNewUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        // WHEN
        ImmutableList<Purchase> purchases = sut.getPurchase(user.id());
        // THEN
        assertThat(purchases).isNotNull();
    }

    @Test
    void returns_purchase_By_random_user_in_repository() {
        // GIVEN
        UUID randomUuid = UUID.randomUUID();

        Exception exception = assertThrows(Exception.class,
                () -> sut.getPurchase(randomUuid));

        assertEquals(UserNotFoundInRepositoryException.class, exception.getClass());
        assertEquals("User with ID " + randomUuid.toString() + " not found", exception.getMessage());

    }

}
