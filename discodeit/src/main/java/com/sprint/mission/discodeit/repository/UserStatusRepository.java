package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    UserStatus findById(UUID id);
    Optional<UserStatus> findByUserId(UUID userId);
    List<UserStatus> findByUserIdIn(List<UUID> userIds);
    List<UserStatus> findAll();
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
}
