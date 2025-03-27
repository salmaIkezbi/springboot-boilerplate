package com.nimbleways.springboilerplate.common.infra.adapters.fakes;

import static com.nimbleways.springboilerplate.testhelpers.helpers.Mapper.toUserPrincipal;

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
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
@Import(FakeDatabase.class)
public class FakeUserRepository implements UserRepositoryPort, UserCredentialsRepositoryPort {
    private final FakeDatabase fakeDb;

    @Override
    public User create(NewUser userToCreate) {
        ensureUserDoesNotExist(userToCreate.email().value());
        User user = toUser(userToCreate);
        fakeDb.userTable.put(user.email().value(), new FakeDatabase.UserWithPassword(user, userToCreate.encodedPassword()));
        return user;
    }

    @Override
    public User update(UpdatedUser userToUpdate) {
        return fakeDb.userTable
                .values()
                .stream()
                .filter(user -> user.user().id().equals(userToUpdate.id()))
                .findFirst()
                .map(userWithPassword -> {
                    User existingUser = userWithPassword.user();
                    User newUser = new User(existingUser.id(),existingUser.name(),existingUser.email(),existingUser.createdAt(),existingUser.role(),existingUser.employmentDate(),userToUpdate.shouldReceiveMailNotifications() , userToUpdate.shouldReceiveApprovalNotifications());
                    fakeDb.userTable.remove(existingUser.email().value());
                    fakeDb.userTable.put(userWithPassword.user().email().value(), new FakeDatabase.UserWithPassword(newUser,userToUpdate.encodedPassword()));
                    return newUser;
                })
                .orElseThrow(() -> new UserNotFoundInRepositoryException(userToUpdate.id().toString(),
                        new IllegalArgumentException("ID utilisateur invalide : " + userToUpdate.id().toString())));
    }

    @Override
    public ImmutableList<User> findAll() {
        return fakeDb.userTable.collect(FakeDatabase.UserWithPassword::user).toImmutableList();
    }

    @Override
    public User findByID(UUID id) {
        return fakeDb.userTable
                .values()
                .stream()
                .filter(user -> user.user().id().equals(id)) // Filtrer par UUID
                .findFirst() // Retourner le premier utilisateur trouvé
                .map(FakeDatabase.UserWithPassword::user)
                .orElseThrow(() -> new UserNotFoundInRepositoryException(id.toString(),
                        new IllegalArgumentException("User not found in the repository")));
    }

    @Override
    public Optional<UserCredential> findUserCredentialByEmail(String email) {
        return Optional
                .ofNullable(fakeDb.userTable.get(email))
                .map(u -> new UserCredential(toUserPrincipal(u.user()), u.encodedPassword()));
    }

    private static User toUser(NewUser userToCreate) {
        return new User(UUID.randomUUID(), userToCreate.name(), userToCreate.email(),
                userToCreate.creationDateTime(),
                userToCreate.role(),
                userToCreate.employmentDate(),
                false,
                false);
    }

    private void ensureUserDoesNotExist(String email) {
        if (fakeDb.userTable.containsKey(email)) {
            throw new EmailAlreadyExistsInRepositoryException(
                    email, new DataIntegrityViolationException(""));
        }
    }


}
