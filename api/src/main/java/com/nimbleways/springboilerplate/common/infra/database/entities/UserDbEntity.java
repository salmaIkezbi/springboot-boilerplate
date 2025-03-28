package com.nimbleways.springboilerplate.common.infra.database.entities;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserCredential;
import com.nimbleways.springboilerplate.features.authentication.domain.entities.UserPrincipal;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("PMD.ExcessiveImports")
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDbEntity {
    @Id
    @Column(name = "id")
    @UuidGenerator
    @NotNull
    private UUID id;

    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "enabled")
    @Nullable
    private Boolean enabled;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "created_at", updatable = false)
    @NotNull
    private Instant createdAt;

    @Column(name = "role")
    @NotNull
    private String role;

    @Column(name = "employment_date")
    @NotNull
    private LocalDate employmentDate;

    @Column(name = "shouldReceiveApprovalNotifications")
    @NotNull
    private Boolean shouldReceiveApprovalNotifications = false;

    @Column(name = "shouldReceiveMailNotifications")
    @NotNull
    private Boolean shouldReceiveMailNotifications = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<PurchaseDbEntity> purchases = new ArrayList<>();

    public static UserDbEntity from(NewUser newUser) {
        String role = newUser.role();
        final UserDbEntity userDbEntity = new UserDbEntity();
        userDbEntity.name(newUser.name());
        userDbEntity.email(newUser.email().value());
        userDbEntity.password(newUser.encodedPassword().value());
        userDbEntity.createdAt(newUser.creationDateTime());
        userDbEntity.role(role);
        userDbEntity.employmentDate(newUser.employmentDate());
        return userDbEntity;
    }

    public User toUser() {
        return new User(
                id,
                name,
                new Email(email),
                createdAt,
                role, employmentDate,
                shouldReceiveMailNotifications,
                shouldReceiveApprovalNotifications);
    }

    public UserPrincipal toUserPrincipal() {
        return new UserPrincipal(
                id,
                new Email(email),
                role);
    }

    public UserCredential toUserCredential() {
        return new UserCredential(toUserPrincipal(), new EncodedPassword(password()));
    }

}
