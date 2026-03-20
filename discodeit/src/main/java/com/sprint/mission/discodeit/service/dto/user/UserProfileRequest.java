package com.sprint.mission.discodeit.service.dto.user;

import lombok.Builder;

@Builder
public record UserProfileRequest(
        byte[] data,
        String fileName,
        String contentType
) {
}
