package com.sprint.mission.discodeit.service.dto.user;

import lombok.Builder;

@Builder
public record CreateUserRequest(
        String username,
        String email,
        String password,
        UserProfileRequest profile
) {
}
