package com.sprint.mission.discodeit.service.dto.readstatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateReadStatusRequest(
        UUID readStatusId,
        Instant lastReadAt
) {
}
