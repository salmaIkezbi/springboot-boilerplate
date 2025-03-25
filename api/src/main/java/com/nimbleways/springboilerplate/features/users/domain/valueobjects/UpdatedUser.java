package com.nimbleways.springboilerplate.features.users.domain.valueobjects;

import com.nimbleways.springboilerplate.common.domain.valueobjects.EncodedPassword;

import java.util.UUID;

public record UpdatedUser(UUID id,
                          EncodedPassword encodedPassword,
                          Boolean shouldReceiveMailNotifications,
                          Boolean shouldReceiveApprovalNotifications) {
}
