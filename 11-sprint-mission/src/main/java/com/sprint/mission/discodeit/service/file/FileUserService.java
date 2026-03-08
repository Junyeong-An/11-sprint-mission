package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class FileUserService extends AbstractFileStore<User> implements UserService {
    public FileUserService() {
        super("users.ser");
    }

    @Override
    public User createUser(String name) {
        List<User> users = readAll();
        User user = new User(name);
        users.add(user);
        writeAll(users);
        return user;
    }

    @Override
    public User findUser(UUID id) {
        List<User> users = readAll();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new IllegalArgumentException("해당 유저를 찾을 수 없어요.");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = readAll();
        return List.copyOf(users);
    }

    @Override
    public void updateName(UUID id, String name) {
        List<User> users = readAll();
        for(User user :  users){
            if(user.getId().equals(id)){
                user.update(name);
                writeAll(users);
                return;
            }
        }
        throw new IllegalArgumentException("해당 유저를 찾을 수 없어요.");
    }

    @Override
    public void delete(UUID id) {
        List<User> users = readAll();
        for(User user : users){
            if(user.getId().equals(id)){
                users.remove(user);
                writeAll(users);
                return;
            }
        }
        throw new IllegalArgumentException("해당 유저를 찾을 수 없어요.");
    }
}
