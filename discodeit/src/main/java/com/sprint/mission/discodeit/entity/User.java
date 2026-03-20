package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID profileId;
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password, UUID profileId) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update( String username, String email, String password) {
        if (username != null) {
            this.username = username;
        }
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        touchUpdatedAt();
    }

    public void replaceProfile(UUID newProfileId) {
        this.profileId = newProfileId;
        touchUpdatedAt();
    }
}
