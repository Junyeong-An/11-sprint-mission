package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import java.util.UUID;
import lombok.Builder;

/**
 * 사용자 응답 DTO
 * - password, createdAt, updatedAt 등 민감하거나 불필요한 필드를 제외하고 API에 노출할 정보만 포함
 */
@Builder
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online
) {
}
