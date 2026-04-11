package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity {
    private String username;
    private String email;
    private String password;
    private BinaryContent profile;
    private UserStatus status;

    @Builder
    public User(String username, String email, String password, BinaryContent profile) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public void assignStatus(UserStatus status) {
        this.status = status;
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
    }

    public void replaceProfile(BinaryContent newProfile) {
        this.profile = newProfile;
    }
}
