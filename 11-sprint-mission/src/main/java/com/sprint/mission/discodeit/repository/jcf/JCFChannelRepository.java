package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> data;

    public JCFChannelRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel save(Channel channel) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(channel.getId())) {
                data.set(i, channel);
                return channel;
            }
        }
        data.add(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("채널을 찾을 수 없어요.");
    }

    @Override
    public List<Channel> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(UUID id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                data.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("채널을 찾을 수 없어요.");
    }
}
