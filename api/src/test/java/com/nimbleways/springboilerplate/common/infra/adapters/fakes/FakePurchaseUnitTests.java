package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.purchases.domain.ports.PurchaseRepositoryPortContractTests;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;

@UnitTest
public class FakePurchaseUnitTests extends PurchaseRepositoryPortContractTests {

    private final FakePurchaseRepository fakePurchaseRepository;

    public FakePurchaseUnitTests() {
        super();
        this.fakePurchaseRepository = Instance.create(FakePurchaseRepository.class);
    }


    @Override
    protected PurchaseRepositoryPort getPurchaseRepository() {
        return fakePurchaseRepository;
    }
}
