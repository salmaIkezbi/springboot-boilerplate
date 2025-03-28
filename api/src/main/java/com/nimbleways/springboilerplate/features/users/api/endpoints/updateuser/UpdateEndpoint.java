package com.nimbleways.springboilerplate.features.users.api.endpoints.updateuser;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;
import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUseCase;
import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUserCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
public class UpdateEndpoint {
    private static final String URL = "/users";

    private final UpdateUseCase updateUseCase;

    public UpdateEndpoint(final UpdateUseCase updateUseCase) {
        this.updateUseCase = updateUseCase;
    }

    @PutMapping(URL+"/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public UpdateResponse createUser(@RequestBody @Valid final UpdateUserRequest updateUserRequest, @PathVariable("id") String id) {
          final UpdateUserCommand updateUserCommand = updateUserRequest.toUpdateUserCommand(id);
          final User user = updateUseCase.handle(updateUserCommand);
          return UpdateResponse.from(user);
    }
}
