package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.message.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final BinaryContentMapper binaryContentMapper;
    private final UserMapper userMapper;

    public MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .content(message.getContent())
                .channelId(message.getChannel().getId())
                .author(userMapper.toDto(message.getAuthor()))
                .attachments(message.getAttachments().stream()
                        .map(binaryContentMapper::toDto)
                        .toList())
                .build();
    }
}
