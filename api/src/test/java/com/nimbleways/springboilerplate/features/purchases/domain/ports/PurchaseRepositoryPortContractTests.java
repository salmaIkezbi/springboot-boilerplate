package com.nimbleways.springboilerplate.features.purchases.domain.ports;


import com.nimbleways.springboilerplate.common.infra.adapters.TimeProvider;
import com.nimbleways.springboilerplate.features.puchases.domain.entities.Purchase;
import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchase;
import com.nimbleways.springboilerplate.features.puchases.domain.valueobjects.NewPurchaseBuilder;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUserBuilder;
import com.nimbleways.springboilerplate.testhelpers.fixtures.NewUserFixture;
import jakarta.transaction.Transactional;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public abstract class PurchaseRepositoryPortContractTests {
    private PurchaseRepositoryPort purchaseRepository;
    private UserRepositoryPort userRepository;

    @BeforeEach
    public void createSut() {
        purchaseRepository = getPurchaseRepository();
        userRepository = getUserRepository();
    }

    @Test
    void creating_a_new_purchase_succeed() {
        NewUser newUser = aNewUser().build();
        User user = userRepository.create(newUser);

        NewPurchase newPurchase = aNewPurchase(user.id()).build();

        purchaseRepository.create(newPurchase);
        NewPurchase newPurchaseFromDb = reconstructNewPurchaseFromDb(newPurchase.userId());
        assertEquals(newPurchase, newPurchaseFromDb);
    }

    @Test
    void creating_a_new_purchase_returns_the_created_purchase() {
        NewUser newUser = aNewUser().build();
        User user = userRepository.create(newUser);

        NewPurchase newPurchase = aNewPurchase(user.id()).build();

        Purchase purchase = purchaseRepository.create(newPurchase);
        assertEquals(List.of(purchase), purchaseRepository.findAll());
    }

    @Test
    void getting_purchases_By_UserID() {
        NewUser newUser = aNewUser().build();
        User user = userRepository.create(newUser);

        NewPurchase newPurchase = aNewPurchase(user.id()).build();

        Purchase purchase = purchaseRepository.create(newPurchase);

        ImmutableList<Purchase> purchases = purchaseRepository.findByUserID(user.id());

        assertEquals(List.of(purchase), purchases);
    }

    @Test
    void getting_purchases_By_randomUserID() {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        NewPurchase newPurchase = aNewPurchase(uuid).build();

        //when
        Exception exception = assertThrows(Exception.class,
                () -> purchaseRepository.create(newPurchase));

        assertEquals(UserNotFoundInRepositoryException.class, exception.getClass());
        assertEquals("User with ID " + uuid + " not found", exception.getMessage());

    }

    private static NewUserBuilder aNewUser() {
        NewUserFixture.UserData userData = new NewUserFixture.UserData.Builder()
                .timeProvider(TimeProvider.UTC)
                .build();
        return NewUserBuilder.aNewUser().userData(userData);
    }

    private static NewPurchaseBuilder aNewPurchase(UUID userId) {
        return NewPurchaseBuilder.aNewPurchase().userId(userId);
    }

    private NewPurchase reconstructNewPurchaseFromDb(UUID userId) {
        Purchase purchase = purchaseRepository.findByUserID(userId).detectOptional(p -> p.userId().equals(userId)).orElseThrow();
        return new NewPurchase(
                userId,
                purchase.brand(),
                purchase.model(),
                purchase.price(),
                purchase.store(),
                purchase.pathImage());
    }



    // --------------------------------- Protected Methods
    // ------------------------------- //
    protected abstract PurchaseRepositoryPort getPurchaseRepository();
    protected abstract UserRepositoryPort getUserRepository();

}
