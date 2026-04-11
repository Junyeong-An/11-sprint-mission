package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Message extends BaseUpdatableEntity {
    private final User author;
    private final Channel channel;
    private String content;
    private final List<BinaryContent> attachments = new ArrayList<>();

    public Message(User author, Channel channel, String content) {
        super();
        this.author = author;
        this.channel = channel;
        this.content = content;
    }

    public Message(User author, Channel channel, String content, List<BinaryContent> attachments) {
        this(author, channel, content);
        if (attachments != null) {
            this.attachments.addAll(attachments);
        }
    }

    public void update(String content) {
        this.content = content;
    }
}
