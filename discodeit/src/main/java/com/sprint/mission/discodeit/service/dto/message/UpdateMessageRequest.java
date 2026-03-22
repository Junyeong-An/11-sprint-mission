package com.sprint.mission.discodeit.service.dto.message;

import java.util.UUID;

public record UpdateMessageRequest(
        UUID messageId,
        String content
) {
}
