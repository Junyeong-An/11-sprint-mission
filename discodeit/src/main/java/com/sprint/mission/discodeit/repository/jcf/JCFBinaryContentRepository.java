package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final List<BinaryContent> data = new ArrayList<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(binaryContent.getId())) {
                data.set(i, binaryContent);
                return binaryContent;
            }
        }
        data.add(binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return data.stream()
                .filter(binaryContent -> binaryContent.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = data.removeIf(binaryContent -> binaryContent.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND);
        }
    }
}
