package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateName(UUID id, String name) {
        Channel channel = channelRepository.findById(id);
        channel.update(name);
        channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.deleteById(id);
    }
}
