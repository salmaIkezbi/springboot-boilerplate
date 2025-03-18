package com.nimbleways.springboilerplate.testhelpers.fixtures;

import com.nimbleways.springboilerplate.common.domain.ports.PasswordEncoderPort;
import com.nimbleways.springboilerplate.common.domain.ports.TimeProviderPort;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.common.infra.adapters.fakes.FakePasswordEncoder;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.NewUser;
import com.nimbleways.springboilerplate.testhelpers.configurations.TimeTestConfiguration;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import org.jilt.Builder;

import static java.util.Objects.requireNonNullElse;

public class NewUserFixture {
    private static final String DEFAULT_ROLE = "USER";
    private static final LocalDate DEFAULT_employment_Date = LocalDate.now();
    private static final FakePasswordEncoder DEFAULT_PASSWORD_ENCODER = new FakePasswordEncoder();
    private static final TimeProviderPort DEFAULT_TIME_PROVIDER = TimeTestConfiguration.fixedTimeProvider();

    @Getter
    public static final class UserData {
        private final String name;
        private final String email;
        private final String plainPassword;
        private final String role;
        private final TimeProviderPort timeProvider;
        private final PasswordEncoderPort passwordEncoder;
        private final LocalDate employmentDate;

        private UserData(Builder builder) {
            this.name = builder.name;
            this.email = builder.email;
            this.plainPassword = builder.plainPassword;
            this.role = builder.role;
            this.timeProvider = builder.timeProvider;
            this.passwordEncoder = null;
            this.employmentDate = null;
        }

        public static class Builder {
            private String name;
            private String email;
            private String plainPassword;
            private String role;
            private TimeProviderPort timeProvider;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder email(String email) {
                this.email = email;
                return this;
            }

            public Builder plainPassword(String plainPassword) {
                this.plainPassword = plainPassword;
                return this;
            }

            public Builder role(String role) {
                this.role = role;
                return this;
            }

            public Builder timeProvider(TimeProviderPort timeProvider) {
                this.timeProvider = timeProvider;
                return this;
            }

            public UserData build() {
                return new UserData(this);
            }
        }
    }

    @Builder(factoryMethod = "aNewUser")
    public static NewUser buildNewUser(UserData userData) {
        UUID id = UUID.randomUUID();
        String nameValue = requireNonNullElse(userData.name, "name-" + id);
        String emailValue = requireNonNullElse(userData.email, "email-" + id);
        String roleValue = requireNonNullElse(userData.role, DEFAULT_ROLE);
        PasswordEncoderPort passwordEncoderValue = requireNonNullElse(userData.passwordEncoder,
                DEFAULT_PASSWORD_ENCODER);
        String plainPasswordValue = requireNonNullElse(userData.plainPassword, "password-" + id);
        TimeProviderPort timeProviderValue = requireNonNullElse(userData.timeProvider, DEFAULT_TIME_PROVIDER);
        LocalDate employmentDateValue = requireNonNullElse(userData.employmentDate, DEFAULT_employment_Date);

        return new NewUser(
                nameValue,
                new Email(emailValue),
                passwordEncoderValue.encode(plainPasswordValue),
                timeProviderValue.instant(),
                roleValue,
                employmentDateValue);
    }
}
