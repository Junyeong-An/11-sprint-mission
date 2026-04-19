package com.sprint.mission.discodeit.service.dto.userstatus;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

/**
 * 유저 온라인 상태 응답 DTO
 */
@Builder
public record UserStatusDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt
) {
}
