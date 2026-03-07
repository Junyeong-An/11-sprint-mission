package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name);
    Channel findChannel(UUID id);
    List<Channel> getAllChannels();
    void updateName(UUID id, String name);
    void delete(UUID id);
}
