package com.nimbleways.springboilerplate.features.users.domain.valueobjects;

import com.nimbleways.springboilerplate.common.domain.valueobjects.Email;
import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import java.time.Instant;
import java.time.LocalDate;

public record NewUser(
        String name,
        Email email,
        EncodedPassword encodedPassword,
        Instant creationDateTime,
        String role,
        LocalDate employmentDate) {
}