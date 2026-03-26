package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory, "user-statuses.ser");
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        List<UserStatus> data = readAll();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(userStatus.getId())) {
                data.set(i, userStatus);
                writeAll(data);
                return userStatus;
            }
        }
        data.add(userStatus);
        writeAll(data);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        return readAll().stream()
                .filter(userStatus -> userStatus.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return readAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findByUserIdIn(List<UUID> userIds) {
        return readAll().stream()
                .filter(userStatus -> userIds.contains(userStatus.getUserId()))
                .toList();
    }

    @Override
    public List<UserStatus> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public void deleteById(UUID id) {
        List<UserStatus> data = readAll();
        boolean removed = data.removeIf(userStatus -> userStatus.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND);
        }
        writeAll(data);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<UserStatus> data = readAll();
        data.removeIf(userStatus -> userStatus.getUserId().equals(userId));
        writeAll(data);
    }
}
