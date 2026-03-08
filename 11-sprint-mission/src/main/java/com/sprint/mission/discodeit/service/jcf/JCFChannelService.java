package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel findChannel(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("해당 채널을 찾을 수 없어요.");
    }

    @Override
    public List<Channel> getAllChannels() {
        return List.copyOf(data);
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = findChannel(id);
        channel.update(name);
    }

    @Override
    public void delete(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                data.remove(channel);
                return;
            }
        }
        throw new IllegalArgumentException("해당 채널을 찾을 수 없어요.");
    }
}
