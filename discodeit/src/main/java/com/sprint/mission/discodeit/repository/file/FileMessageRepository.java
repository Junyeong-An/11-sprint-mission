package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {

    public FileMessageRepository() {
        super("messages.ser");
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
        throw new IllegalArgumentException("Message not found.");
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(readAll());
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
        throw new IllegalArgumentException("Message not found.");
    }
}
