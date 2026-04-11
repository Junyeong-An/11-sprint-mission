package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final List<UserStatus> data = new ArrayList<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(userStatus.getId())) {
                data.set(i, userStatus);
                return userStatus;
            }
        }
        data.add(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        return data.stream()
                .filter(userStatus -> userStatus.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.stream()
                .filter(userStatus -> userStatus.getUser().getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findByUserIdIn(List<UUID> userIds) {
        return data.stream()
                .filter(userStatus -> userIds.contains(userStatus.getUser().getId()))
                .toList();
    }

    @Override
    public List<UserStatus> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = data.removeIf(userStatus -> userStatus.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.removeIf(userStatus -> userStatus.getUser().getId().equals(userId));
    }
}
