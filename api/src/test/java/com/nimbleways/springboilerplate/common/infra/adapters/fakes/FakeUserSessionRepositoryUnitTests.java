package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserSession;
import com.nimbleways.springboilerplate.features.authentication.domain.ports.UserSessionRepositoryPort;
import com.nimbleways.springboilerplate.features.authentication.domain.ports.UserSessionRepositoryPortContractTests;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import com.nimbleways.springboilerplate.testhelpers.annotations.UnitTest;
import com.nimbleways.springboilerplate.testhelpers.utils.BeanBag;
import com.nimbleways.springboilerplate.testhelpers.utils.Instance;
import org.eclipse.collections.api.list.ImmutableList;

@UnitTest
public class FakeUserSessionRepositoryUnitTests extends UserSessionRepositoryPortContractTests {

    private final FakeUserRepository fakeUserRepository;
    private final FakeUserSessionRepository fakeUserSessionRepository;

    public FakeUserSessionRepositoryUnitTests() {
        super();
        BeanBag beans = Instance.create(FakeUserRepository.class, FakeUserSessionRepository.class);
        fakeUserRepository = beans.get(FakeUserRepository.class);
        fakeUserSessionRepository = beans.get(FakeUserSessionRepository.class);
    }

    @Override
    protected UserSessionRepositoryPort getUserSessionRepository() {
        return fakeUserSessionRepository;
    }

    @Override
    protected UserRepositoryPort getUserRepository() {
        return fakeUserRepository;
    }

    @Override
    protected ImmutableList<UserSession> getAllUserSessions() {
        return fakeUserSessionRepository.findAll();
    }
}
