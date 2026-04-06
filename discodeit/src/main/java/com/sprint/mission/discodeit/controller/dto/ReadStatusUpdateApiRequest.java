package com.sprint.mission.discodeit.controller.dto;

import java.time.Instant;

public record ReadStatusUpdateApiRequest(
        Instant newLastReadAt
) {
}
