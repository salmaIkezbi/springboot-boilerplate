package com.nimbleways.springboilerplate.features.users.api.endpoints.updateuser;

import com.nimbleways.springboilerplate.features.users.domain.entities.User;

import java.time.LocalDate;

public record UpdateResponse(String id,
                             String name,
                             String email,
                             String role,
                             LocalDate employmentDate,
                             Boolean shouldReceiveMailNotifications,
                             Boolean shouldReceiveApprovalNotifications) {
    public static UpdateResponse from(User user) {
        return new UpdateResponse(
                user.id().toString(),
                user.name(),
                user.email().value(),
                user.role(),
                user.employmentDate(),
                user.shouldReceiveMailNotifications(),
                user.shouldReceiveApprovalNotifications());
    }
}