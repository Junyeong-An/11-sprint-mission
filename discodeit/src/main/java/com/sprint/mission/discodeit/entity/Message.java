package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID authorId;
    private final UUID channelId;

    private String content;

    public Message(UUID authorId, UUID channelId, String content) {
        super();
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }


    public void update(String content) {
        this.content = content;
        touchUpdatedAt();
    }
}
