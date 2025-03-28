package com.nimbleways.springboilerplate.testhelpers.fixtures;

import com.nimbleways.springboilerplate.common.domain.ports.TimeProviderPort;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.testhelpers.configurations.TimeTestConfiguration;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.annotation.Nullable;
import org.jilt.Builder;

import static java.util.Objects.requireNonNullElse;

public class UserFixture {
    private static final String DEFAULT_ROLE = "";
    private static final TimeProviderPort DEFAULT_TIME_PROVIDER = TimeTestConfiguration.fixedTimeProvider();
    private static final LocalDate DEFAULT_employment_Date = LocalDate.now();

    @Builder(factoryMethod = "aUser")
    public static User buildUser(
            @Nullable UUID id,
            @Nullable String name,
            @Nullable String email,
            @Nullable TimeProviderPort timeProvider,
            @Nullable String role,
            @Nullable LocalDate employmentDate,
            @Nullable Boolean shouldReceiveMailNotifications,
            @Nullable Boolean shouldReceiveApprovalNotifications) {
        UUID idValue = requireNonNullElse(id, UUID.randomUUID());
        String nameValue = requireNonNullElse(name, "name");
        String emailValue = requireNonNullElse(email, "email");
        TimeProviderPort timeProviderValue = requireNonNullElse(timeProvider, DEFAULT_TIME_PROVIDER);
        String roleValue = requireNonNullElse(role, DEFAULT_ROLE);
        LocalDate employmentDateValue = requireNonNullElse(employmentDate, DEFAULT_employment_Date);
        Boolean shouldReceiveMailNotificationsValue = requireNonNullElse(shouldReceiveMailNotifications , false);
        Boolean shouldReceiveApprovalNotificationsValue = requireNonNullElse(shouldReceiveApprovalNotifications, false);

        return new User(
                idValue,
                nameValue,
                new Email(emailValue),
                timeProviderValue.instant(),
                roleValue,
                employmentDateValue,
                shouldReceiveMailNotificationsValue,
                shouldReceiveApprovalNotificationsValue);
    }
}
