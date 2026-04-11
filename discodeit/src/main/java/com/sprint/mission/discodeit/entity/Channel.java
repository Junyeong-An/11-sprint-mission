package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

@Getter
public class Channel extends BaseUpdatableEntity {
    private String name;
    private String description;
    private ChannelType type;

    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Channel publicChannel(String name, String description) {
        return new Channel(name, description, ChannelType.PUBLIC);
    }

    public static Channel privateChannel() {
        return new Channel(null, null, ChannelType.PRIVATE);
    }
}
