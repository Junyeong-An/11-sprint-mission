package com.sprint.mission.discodeit.service.dto.message;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * 메시지 응답 DTO
 * - author는 UserDto로 임베딩(ID 참조가 아닌 전체 정보 포함)
 * - attachments는 BinaryContentDto 리스트로 포함
 */
@Builder
public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserDto author,
        List<BinaryContentDto> attachments
) {
}
