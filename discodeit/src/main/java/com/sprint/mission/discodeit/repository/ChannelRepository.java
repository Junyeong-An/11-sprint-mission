package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 채널 JPA 레포지토리 - 기본 CRUD는 JpaRepository가 제공
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

}
