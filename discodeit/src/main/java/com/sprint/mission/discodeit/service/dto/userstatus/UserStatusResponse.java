package com.sprint.mission.discodeit.service.dto.userstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusResponse(
        UUID id,
        UUID userId,
        Instant lastConnectedAt,
        boolean online,
        Instant createdAt,
        Instant updatedAt
) {
}
