package com.sprint.mission.discodeit.controller.dto;

import java.time.Instant;

public record UserStatusUpdateApiRequest(
        Instant newLastActiveAt
) {
}
