package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;

@Getter
public class UserStatus extends BaseUpdatableEntity {
    private static final Duration ONLINE_THRESHOLD = Duration.ofMinutes(5);

    private final User user;
    private Instant lastActiveAt;

    public UserStatus(User user) {
        super();
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt() {
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public boolean isOnline() {
        Instant onlineCutoff = Instant.now().minus(ONLINE_THRESHOLD);
        return !lastActiveAt.isBefore(onlineCutoff);
    }
}
