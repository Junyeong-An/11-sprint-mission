package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory, "messages.ser");
    }

    @Override
    public Message save(Message message) {
        List<Message> messages = readAll();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(message.getId())) {
                messages.set(i, message);
                writeAll(messages);
                return message;
            }
        }
        messages.add(message);
        writeAll(messages);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        List<Message> messages = readAll();
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        throw new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND);
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return readAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        List<Message> messages = readAll();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(id)) {
                messages.remove(i);
                writeAll(messages);
                return;
            }
        }
        throw new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<Message> messages = readAll();
        boolean removed = messages.removeIf(message -> message.getChannelId().equals(channelId));
        if (removed) {
            writeAll(messages);
        }
    }
}
