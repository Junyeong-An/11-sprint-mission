package com.sprint.mission.discodeit.service.dto.user;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        UUID profileId,
        boolean online,
        Instant createdAt,
        Instant updatedAt
) {
}
