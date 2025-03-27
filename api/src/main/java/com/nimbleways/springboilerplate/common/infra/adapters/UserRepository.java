package com.nimbleways.springboilerplate.common.infra.adapters;

import com.nimbleways.springboilerplate.common.infra.database.entities.UserDbEntity;
import com.nimbleways.springboilerplate.common.infra.database.jparepositories.JpaUserRepository;
import com.nimbleways.springboilerplate.common.utils.collections.Immutable;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserCredential;
import com.nimbleways.springboilerplate.features.authentication.domain.ports.UserCredentialsRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.EmailAlreadyExistsInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.exceptions.UserNotFoundInRepositoryException;
import com.nimbleways.springboilerplate.features.users.domain.ports.UserRepositoryPort;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import java.util.Optional;
import java.util.UUID;

import com.nimbleways.springboilerplate.features.users.domain.valueobjects.UpdatedUser;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class UserRepository implements UserRepositoryPort, UserCredentialsRepositoryPort {
    private final JpaUserRepository jpaUserRepository;

    public UserRepository(final JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User create(NewUser userToCreate) {
        UserDbEntity userDbEntity = UserDbEntity.from(userToCreate);
        UserDbEntity savedUserDbEntity;
        try {
            savedUserDbEntity = jpaUserRepository.saveAndFlush(userDbEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyExistsInRepositoryException(userToCreate.email().value(), ex);
        }
        return savedUserDbEntity.toUser();
    }

    @Override
    public User update(UpdatedUser userToUpdate) {
        return jpaUserRepository.findById(userToUpdate.id())
                .map(existingUser -> {existingUser.password(userToUpdate.encodedPassword().value());
                    existingUser.shouldReceiveMailNotifications(userToUpdate.shouldReceiveMailNotifications());
                    existingUser.shouldReceiveApprovalNotifications(userToUpdate.shouldReceiveApprovalNotifications());
                    return jpaUserRepository.save(existingUser);
                })
                .orElseThrow(() -> new UserNotFoundInRepositoryException(userToUpdate.id().toString(), new IllegalArgumentException("bad user id "))).toUser();
    }

    @Override
    public ImmutableList<User> findAll() {
        return Immutable.collectList(jpaUserRepository.findAll(), UserDbEntity::toUser);
    }

    @Override
    public Optional<User> findByID(UUID id) {
        return  jpaUserRepository.findById(id).map(UserDbEntity::toUser);
    }

    @Override
    // TODO: Retrieve from the database only the fields needed for UserCredential
    public Optional<UserCredential> findUserCredentialByEmail(String email) {
        return jpaUserRepository
                .findByEmail(email)
                .map(UserDbEntity::toUserCredential);
    }
}
