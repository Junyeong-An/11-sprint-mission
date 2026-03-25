package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private ChannelType channelType;


    public Channel(String name, String description, ChannelType channelType) {
        super();
        this.name = name;
        this.description = description;
        this.channelType = channelType;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
        touchUpdatedAt();
    }


    public static Channel publicChannel(String name, String description) {
        return new Channel(name, description, ChannelType.PUBLIC);

    }

    public static Channel privateChannel() {
        return new Channel(null,null,ChannelType.PRIVATE);
    }
}
    
