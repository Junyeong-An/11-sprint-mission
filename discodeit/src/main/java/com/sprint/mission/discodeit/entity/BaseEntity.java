package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class BaseEntity{
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    protected void touchUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
