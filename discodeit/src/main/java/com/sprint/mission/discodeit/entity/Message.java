package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {
    private final UUID id;
    private static final long serialVersionUID = 1L;
    private final Long createdAt;
    private Long updatedAt;
    private final UUID authorId;
    private final UUID channelId;

    private String content;

    public Message(UUID authorId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }


    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
