package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public Channel(String name){
        super();
        this.name = name;
    }
    
    public void update(String name){
        this.name = name;
        touchUpdatedAt();
    }
}
