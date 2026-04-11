package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseEntity {
    private final UUID id;
    private final Instant createdAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
