package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final List<Message> data;

    public JCFMessageRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public Message save(Message message) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(message.getId())) {
                data.set(i, message);
                return message;
            }
        }
        data.add(message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        throw new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND);
    }

    @Override
    public List<Message> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                return;
            }
        }
        throw new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.removeIf(message -> message.getChannelId().equals(channelId));
    }
}
