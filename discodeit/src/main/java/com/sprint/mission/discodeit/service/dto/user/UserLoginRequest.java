package com.sprint.mission.discodeit.service.dto.user;

public record UserLoginRequest(
        String username,
        String password
) {
}
