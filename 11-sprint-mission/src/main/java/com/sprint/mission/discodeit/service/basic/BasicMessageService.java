package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message createMessage(UUID authorId, UUID channelId, String content) {
        userRepository.findById(authorId);
        channelRepository.findById(channelId);

        Message message = new Message(authorId, channelId, content);
        return messageRepository.save(message);
    }

    @Override
    public Message findMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateContent(UUID id, String content) {
        Message message = messageRepository.findById(id);
        message.update(content);
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.deleteById(id);
    }
}