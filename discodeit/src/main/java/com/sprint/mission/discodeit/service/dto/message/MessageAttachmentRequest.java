package com.sprint.mission.discodeit.service.dto.message;

public record MessageAttachmentRequest(
        byte[] data,
        String fileName,
        String contentType
) {
}
