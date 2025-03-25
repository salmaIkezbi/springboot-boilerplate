package com.nimbleways.springboilerplate.features.users.domain.usecases.updateuser;


import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;
import com.nimbleways.springboilerplate.features.users.domain.valueobjects.UpdatedUser;

import java.util.UUID;

public record UpdateUserCommand(
        String password,
        boolean shouldReceiveMailNotifications,
        boolean shouldReceiveApprovalNotifications,
        UUID id
) {

    public UpdatedUser toUpdatedUser(EncodedPassword encodedPassword) {
        return new UpdatedUser(
                id,
                encodedPassword,
                shouldReceiveMailNotifications,
                shouldReceiveApprovalNotifications);
    }
}
