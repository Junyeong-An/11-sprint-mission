package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseEntity {
    private static final Duration ONLINE_THRESHOLD = Duration.ofMinutes(5);

    private final UUID userId;
    private Instant lastConnectedAt;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
        this.lastConnectedAt = Instant.now();
    }

    public void updateLastConnectedAt() {
        this.lastConnectedAt = Instant.now();
        touchUpdatedAt();
    }

    public boolean isOnline() {
        Instant onlineCutoff = Instant.now().minus(ONLINE_THRESHOLD);
        return !lastConnectedAt.isBefore(onlineCutoff);
    }
}
