package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// 메시지 JPA 레포지토리
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // 채널 ID로 메시지 목록 조회
    List<Message> findAllByChannelId(UUID channelId);

    // 채널 ID로 메시지 페이지 조회
    Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    // 채널 ID로 메시지 일괄 삭제
    void deleteAllByChannelId(UUID channelId);
}
