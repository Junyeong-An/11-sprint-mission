package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements BinaryContentRepository {

    public FileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory, "binary-contents.ser");
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        List<BinaryContent> data = readAll();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(binaryContent.getId())) {
                data.set(i, binaryContent);
                writeAll(data);
                return binaryContent;
            }
        }
        data.add(binaryContent);
        writeAll(data);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return readAll().stream()
                .filter(binaryContent -> binaryContent.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public void deleteById(UUID id) {
        List<BinaryContent> data = readAll();
        boolean removed = data.removeIf(binaryContent -> binaryContent.getId().equals(id));
        if (!removed) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND);
        }
        writeAll(data);
    }
}
