package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 메시지 JPA 레포지토리
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // 채널 ID로 메시지 목록 조회 (channel.id 프로퍼티 경로로 해석됨)
    List<Message> findAllByChannelId(UUID channelId);

    // 채널 ID로 메시지 일괄 삭제
    // 주의: 파생 삭제 쿼리는 bulk delete가 아니라 엔티티를 로드하여 삭제하므로
    // CascadeType 및 orphanRemoval이 정상 동작한다.
    void deleteAllByChannelId(UUID channelId);
}
