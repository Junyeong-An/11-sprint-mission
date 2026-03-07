package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String name);
    User findUser(UUID id);
    List<User> getAllUsers();
    void updateName(UUID id,String name);
    void delete(UUID id);
}
