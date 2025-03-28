package com.nimbleways.springboilerplate.features.users.domain.ports;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.UpdatedUser;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User create(NewUser userToCreate);
    User update(UpdatedUser userToUpdate);
    ImmutableList<User> findAll();
    Optional<User> findByID(UUID id);
}
