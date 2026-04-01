package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory, "read-statuses.ser");
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        List<ReadStatus> data = readAll();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(readStatus.getId())) {
                data.set(i, readStatus);
                writeAll(data);
                return readStatus;
            }
        }
        data.add(readStatus);
        writeAll(data);
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readAll().stream()
                .filter(readStatus -> readStatus.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND));
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return readAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return readAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public void deleteById(UUID id) {
        List<ReadStatus> data = readAll();
        boolean removed = data.removeIf(readStatus -> readStatus.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND);
        }
        writeAll(data);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> data = readAll();
        data.removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
        writeAll(data);
    }
}
