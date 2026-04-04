package com.sprint.mission.discodeit.controller.dto;

public record UserUpdateApiRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}
