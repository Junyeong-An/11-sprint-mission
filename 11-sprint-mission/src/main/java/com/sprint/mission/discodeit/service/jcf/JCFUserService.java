package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public User createUser(String name) {
        User user = new User(name);
        data.add(user);
        return user;
    }

    @Override
    public User findUser(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new IllegalArgumentException("해당 유저가 없어요.");
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(data);
    }

    @Override
    public void updateName(UUID id, String name) {
        User user = findUser(id);
        user.update(name);
    }

    @Override
    public void delete(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                data.remove(user);
                return;
            }
        }
        throw new IllegalArgumentException("해당하는 유저를 찾을 수 없어요.");
    }
}
