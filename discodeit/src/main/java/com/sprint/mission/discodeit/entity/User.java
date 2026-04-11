package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity {
    private UUID profileId;
    private String username;
    private String email;
    private String password;

    @Builder
    public User(String username, String email, String password, UUID profileId) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String username, String email, String password) {
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
