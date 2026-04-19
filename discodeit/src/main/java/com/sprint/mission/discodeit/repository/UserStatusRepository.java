package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 유저상태 JPA 레포지토리
public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    // 유저 ID로 유저상태 단건 조회
    Optional<UserStatus> findByUserId(UUID userId);

    // 유저 ID 목록으로 유저상태 조회
    List<UserStatus> findAllByUserIdIn(List<UUID> userIds);

    // 유저 ID로 유저상태 삭제
    void deleteByUserId(UUID userId);
}
