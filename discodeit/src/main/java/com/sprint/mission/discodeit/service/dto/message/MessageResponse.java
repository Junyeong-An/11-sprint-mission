package com.sprint.mission.discodeit.service.dto.message;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageResponse(
        UUID id,
        UserResponse author,
        UUID channelId,
        String content,
        List<BinaryContentResponse> attachments,
        Instant createdAt,
        Instant updatedAt
) {
}
