package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    List<ReadStatus> findByChannelId(UUID channelId);
    List<ReadStatus> findByUserId(UUID userId);
    List<ReadStatus> findAll();
    void deleteById(UUID id);
    void deleteByChannelId(UUID channelId);
}
