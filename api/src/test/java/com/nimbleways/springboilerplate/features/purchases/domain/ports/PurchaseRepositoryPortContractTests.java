package com.nimbleways.springboilerplate.features.purchases.domain.ports;


import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public abstract class PurchaseRepositoryPortContractTests {
    private PurchaseRepositoryPort purchaseRepository;

    @BeforeEach
    public void createSut() {
        purchaseRepository = getPurchaseRepository();
    }




    // --------------------------------- Protected Methods
    // ------------------------------- //
    protected abstract PurchaseRepositoryPort getPurchaseRepository();

}
