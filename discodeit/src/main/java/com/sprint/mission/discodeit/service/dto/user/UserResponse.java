package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        BinaryContentResponse profile,
        boolean online,
        Instant createdAt,
        Instant updatedAt
) {
}
