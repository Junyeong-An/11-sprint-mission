package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private final UUID id;
    private static final long serialVersionUID = 1L;
    private final Long createdAt;
    private long updatedAt;

    private String name;

    public User(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;

        this.name = name;
    }

    public UUID getId(){
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return name;
    }

    public void update(String name){
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}
