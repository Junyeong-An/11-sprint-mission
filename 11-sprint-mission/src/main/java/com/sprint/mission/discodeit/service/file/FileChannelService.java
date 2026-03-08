package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class FileChannelService extends AbstractFileStore<Channel> implements ChannelService {
    public FileChannelService() {
        super("channels.ser");
    }

    @Override
    public Channel createChannel(String name) {
        List<Channel> channels = readAll();
        Channel channel = new Channel(name);
        channels.add(channel);
        writeAll(channels);
        return channel;
    }

    @Override
    public Channel findChannel(UUID id) {
        List<Channel> channels = readAll();
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("해당 채널을 찾을 수 없어요.");
    }

    @Override
    public List<Channel> getAllChannels() {
        List<Channel> channels = readAll();
        return List.copyOf(channels);
    }

    @Override
    public void updateName(UUID id, String name) {
        List<Channel> channels = readAll();
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                channel.update(name);
                writeAll(channels);
                return;
            }
        }
        throw new IllegalArgumentException("해당 채널을 찾을 수 없어요.");
    }

    @Override
    public void delete(UUID id) {
        List<Channel> channels = readAll();
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                channels.remove(channel);
                writeAll(channels);
                return;
            }
        }
        throw new IllegalArgumentException("해당 채널을 찾을 수 없어요.");
    }
}
