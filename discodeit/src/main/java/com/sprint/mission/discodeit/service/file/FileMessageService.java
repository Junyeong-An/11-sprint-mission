package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class FileMessageService extends AbstractFileStore<Message> implements MessageService {
    public FileMessageService() {
        super("Messages.ser");
    }

    @Override
    public Message createMessage(UUID authorId, UUID channelId, String content) {
        List<Message> messages = readAll();
        Message message = new Message(authorId, channelId, content);
        messages.add(message);
        writeAll(messages);
        return message;
    }

    @Override
    public Message findMessage(UUID id) {
        List<Message> messages = readAll();
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        throw new IllegalArgumentException("해당 메세지를 찾지 못했어요.");
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = readAll();
        return List.copyOf(messages);
    }

    @Override
    public void updateContent(UUID id, String content) {
        List<Message> messages = readAll();
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                message.update(content);
                writeAll(messages);
                return;
            }
        }
        throw new IllegalArgumentException("해당 메세지를 찾지 못했어요.");
    }

    @Override
    public void delete(UUID id) {
        List<Message> messages = readAll();
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                messages.remove(message);
                writeAll(messages);
                return;
            }
        }
        throw new IllegalArgumentException("해당 메세지를 찾지 못했어요.");
    }
}
