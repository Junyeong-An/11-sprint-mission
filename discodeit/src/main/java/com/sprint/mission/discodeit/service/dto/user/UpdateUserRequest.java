package com.sprint.mission.discodeit.service.dto.user;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateUserRequest(
        UUID userId,
        String username,
        String email,
        String password,
        UserProfileRequest replacementProfile
) {
}
