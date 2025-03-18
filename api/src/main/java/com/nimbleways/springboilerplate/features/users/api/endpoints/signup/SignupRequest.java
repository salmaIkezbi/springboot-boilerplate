package com.nimbleways.springboilerplate.features.users.api.endpoints.signup;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.common.domain.valueobjects.Role;
import com.nimbleways.springboilerplate.common.api.annotations.Parsable;
import com.nimbleways.springboilerplate.common.infra.mappers.RoleMapper;
import com.nimbleways.springboilerplate.features.users.domain.usecases.signup.SignupCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SignupRequest(
                @NotBlank String name,
                @NotBlank String email,
                @NotBlank String password,
                @NotNull @Parsable(RoleMapper.class) String role,
                @NotNull LocalDate employmentDate) {
        public SignupCommand toSignupCommand() {
                return new SignupCommand(name(), new Email(email()), password(), role, employmentDate());
        }
}