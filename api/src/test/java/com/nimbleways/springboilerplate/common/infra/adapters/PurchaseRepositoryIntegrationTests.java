package com.nimbleways.springboilerplate.common.infra.adapters;


import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.purchases.domain.ports.PurchaseRepositoryPortContractTests;
import com.nimbleways.springboilerplate.testhelpers.annotations.SetupDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SetupDatabase
@Import({PurchaseRepository.class})
@DataJpaTest
public class PurchaseRepositoryIntegrationTests extends PurchaseRepositoryPortContractTests {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    protected PurchaseRepositoryPort getPurchaseRepository() {
        return purchaseRepository;
    }
}
