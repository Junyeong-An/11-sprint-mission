package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final List<ReadStatus> data = new ArrayList<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(readStatus.getId())) {
                data.set(i, readStatus);
                return readStatus;
            }
        }
        data.add(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return data.stream()
                .filter(readStatus -> readStatus.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND));
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return data.stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return data.stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = data.removeIf(readStatus -> readStatus.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
    }
}
