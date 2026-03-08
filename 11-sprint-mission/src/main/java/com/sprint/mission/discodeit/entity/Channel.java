package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private final UUID id;
    private static final long serialVersionUID = 1L;
    private final Long createdAt;
    private Long updatedAt;

    private String name;

    public Channel(String name){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return name;
    }
    
    public void update(String name){
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}
