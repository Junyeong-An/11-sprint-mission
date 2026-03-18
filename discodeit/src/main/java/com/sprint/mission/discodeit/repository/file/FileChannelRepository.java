package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    public FileChannelRepository() {
        super("channels.ser");
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = readAll();
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(channel.getId())) {
                channels.set(i, channel);
                writeAll(channels);
                return channel;
            }
        }
        channels.add(channel);
        writeAll(channels);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        List<Channel> channels = readAll();
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Channel not found.");
    }

    @Override
    public List<Channel> findAll() {
        return List.copyOf(readAll());
    }

    @Override
    public void deleteById(UUID id) {
        List<Channel> channels = readAll();
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(id)) {
                channels.remove(i);
                writeAll(channels);
                return;
            }
        }
        throw new IllegalArgumentException("Channel not found.");
    }
}
