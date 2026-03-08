package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Message createMessage(String content) {
        Message message = new Message(content);
        data.add(message);
        return message;
    }

    @Override
    public Message findMessage(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        throw new IllegalArgumentException("해당하는 메세지를 찾을 수 없어요.");
    }

    @Override
    public List<Message> getAllMessages() {
        return List.copyOf(data);
    }

    @Override
    public void updateContent(UUID id, String content) {
        Message message = findMessage(id);
        message.update(content);
    }

    @Override
    public void delete(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                data.remove(message);
                return;
            }
        }
        throw new IllegalArgumentException("해당하는 메세지를 찾을 수 없어요.");
    }
}
