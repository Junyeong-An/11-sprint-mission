package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID authorId, UUID channelId, String content);
    Message findMessage(UUID id);
    List<Message> getAllMessages();
    void updateContent(UUID id, String content);
    void delete(UUID id);
}
