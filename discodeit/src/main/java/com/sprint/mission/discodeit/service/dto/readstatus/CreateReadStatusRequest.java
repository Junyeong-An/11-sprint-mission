package com.sprint.mission.discodeit.service.dto.readstatus;

import java.util.UUID;

public record CreateReadStatusRequest(
        UUID userId,
        UUID channelId
) {
}
