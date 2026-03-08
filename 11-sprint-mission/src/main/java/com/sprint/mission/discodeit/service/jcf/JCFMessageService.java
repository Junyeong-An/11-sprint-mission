package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(UUID authorId, UUID channelId, String content) {
        userService.findUser(authorId);
        channelService.findChannel(channelId);

        Message message = new Message(authorId, channelId, content);
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
        throw new IllegalArgumentException("해당 메시지를 찾을 수 없어요.");
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
        throw new IllegalArgumentException("해당 메시지를 찾을 수 없어요.");
    }
}
