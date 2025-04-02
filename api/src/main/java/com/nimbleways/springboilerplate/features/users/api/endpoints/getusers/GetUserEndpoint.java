package com.nimbleways.springboilerplate.features.users.api.endpoints.getusers;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.getuser.GetUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GetUserEndpoint {
    private static final String URL = "/user";

    private final GetUserUseCase getUserUseCase;

    public GetUserEndpoint(final GetUserUseCase getUserUseCase) {
        this.getUserUseCase = getUserUseCase;
    }

    @GetMapping(URL + "/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public GetUserResponse.Item getUser(@PathVariable("id") String id) {
        UUID userId = UUID.fromString(id);
        User user = getUserUseCase.handle(userId);
        return GetUserResponse.fromUser(user);
    }

}
