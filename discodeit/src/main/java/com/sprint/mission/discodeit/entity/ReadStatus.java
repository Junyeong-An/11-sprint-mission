package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity{
    private final UUID userId;
    private final UUID channelId;

    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    public void markAsReadNow(){
        this.lastReadAt = Instant.now();
        touchUpdatedAt();
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        touchUpdatedAt();
    }
}
