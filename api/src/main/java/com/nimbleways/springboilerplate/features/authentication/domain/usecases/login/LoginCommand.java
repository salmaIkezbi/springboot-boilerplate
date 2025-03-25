package com.nimbleways.springboilerplate.features.authentication.domain.usecases.login;

public record LoginCommand(
                String email,
                String password) {
}
