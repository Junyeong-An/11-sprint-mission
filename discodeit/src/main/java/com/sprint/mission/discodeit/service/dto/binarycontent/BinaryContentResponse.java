package com.sprint.mission.discodeit.service.dto.binarycontent;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentResponse(
        UUID id,
        Instant createdAt,
        String fileName,
        long size,
        String contentType,
        byte[] bytes
) {
}
