package com.nimbleways.springboilerplate.common.domain.valueobjects;

import jakarta.validation.constraints.Pattern;

public record Email(
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@theodo\\.com$", message = "L'email doit se terminer par @theodo.com")
        String value) {}
