package com.nimbleways.springboilerplate.features.users.api.endpoints.signup;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;

import java.time.LocalDate;

public record SignupResponse(
        String id,
        String name,
        String email,
        String role,
        LocalDate employmentDate) {
    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.id().toString(),
                user.name(),
                user.email().value(),
                user.role(),
                user.employmentDate());
    }
}