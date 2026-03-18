package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final List<User> data;

    public JCFUserRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(user.getId())) {
                data.set(i, user);
                return user;
            }
        }
        data.add(user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new IllegalArgumentException("유저를 찾을 수 없어요.");
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(UUID id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("유저를 찾을 수 없어요.");
    }
}
