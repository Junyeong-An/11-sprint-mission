package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {

    public FileUserRepository() {
        super("users.ser");
    }

    @Override
    public User save(User user) {
        List<User> users = readAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                writeAll(users);
                return user;
            }
        }
        users.add(user);
        writeAll(users);
        return user;
    }

    @Override
    public User findById(UUID id) {
        List<User> users = readAll();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new IllegalArgumentException("User not found.");
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public void deleteById(UUID id) {
        List<User> users = readAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
                writeAll(users);
                return;
            }
        }
        throw new IllegalArgumentException("User not found.");
    }
}
