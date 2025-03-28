package com.nimbleways.springboilerplate.features.users.api.endpoints.getusers;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import lombok.Getter;

@Getter
public final class GetUserResponse {
    public record Item(String id, String name, String email) {}

    private GetUserResponse() {
    }

    public static Item fromUser(User user) {
        return new Item(
                user.id().toString(),
                user.name(),
                user.email().value()
        );
    }
}