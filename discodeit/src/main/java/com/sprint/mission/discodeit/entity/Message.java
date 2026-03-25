package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID authorId;
    private final UUID channelId;

    private String content;
    private final List<UUID> attachmentIds = new ArrayList<>();

    public Message(UUID authorId, UUID channelId, String content) {
        super();
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }

    public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
        this(authorId, channelId, content);
        if (attachmentIds != null) {
            this.attachmentIds.addAll(attachmentIds);
        }
    }

    public void update(String content) {
        this.content = content;
        touchUpdatedAt();
    }
}
