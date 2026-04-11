package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ChannelResponse(
        UUID id,
        String name,
        String description,
        ChannelType type,
        Instant lastMessageAt,
        List<UserResponse> participants,
        Instant createdAt,
        Instant updatedAt
) {
}
