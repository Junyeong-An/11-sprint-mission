package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {
    private Instant updatedAt;

    protected BaseUpdatableEntity() {
        super();
        this.updatedAt = getCreatedAt();
    }

    protected void touchUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
