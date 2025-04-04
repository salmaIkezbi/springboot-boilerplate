package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.features.puchases.domain.ports.PurchaseRepositoryPort;
import com.nimbleways.springboilerplate.features.purchases.domain.ports.PurchaseRepositoryPortContractTests;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.utils.BeanBag;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;

@UnitTest
public class FakePurchaseUnitTests extends PurchaseRepositoryPortContractTests {

    private final FakePurchaseRepository fakePurchaseRepository;
    private final FakeUserRepository fakeUserRepository;

    public FakePurchaseUnitTests() {
        super();
        BeanBag beans = Instance.create(FakePurchaseRepository.class,FakeUserRepository.class);
        this.fakePurchaseRepository = beans.get(FakePurchaseRepository.class);
        this.fakeUserRepository = beans.get(FakeUserRepository.class);
    }


    @Override
    protected PurchaseRepositoryPort getPurchaseRepository() {
        return fakePurchaseRepository;
    }

    @Override
    protected UserRepositoryPort getUserRepository() {
        return fakeUserRepository;
    }
}
