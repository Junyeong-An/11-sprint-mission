package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 읽음상태 JPA 레포지토리
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    // 채널 ID로 읽음상태 목록 조회
    List<ReadStatus> findAllByChannelId(UUID channelId);

    // 유저 ID로 읽음상태 목록 조회
    List<ReadStatus> findAllByUserId(UUID userId);

    // 유저/채널 조합으로 존재 여부 확인 (중복 검사용)
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    // 채널 ID로 읽음상태 일괄 삭제
    void deleteAllByChannelId(UUID channelId);
}
