package com.sprint.mission.discodeit.controller.dto;

public record ChannelUpdateApiRequest(
        String newName,
        String newDescription
) {
}
