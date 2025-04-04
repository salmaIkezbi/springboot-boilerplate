package com.nimbleways.springboilerplate.features.users.api.endpoints.updateuser;

import com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser.UpdateUserCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserRequest(
        @NotBlank String password,
        @NotNull Boolean shouldReceiveMailNotifications,
        @NotNull Boolean shouldReceiveApprovalNotifications
) {
    public UpdateUserCommand toUpdateUserCommand(String id) {
        return new UpdateUserCommand(password,shouldReceiveMailNotifications,shouldReceiveApprovalNotifications,UUID.fromString(id));
    }

}
