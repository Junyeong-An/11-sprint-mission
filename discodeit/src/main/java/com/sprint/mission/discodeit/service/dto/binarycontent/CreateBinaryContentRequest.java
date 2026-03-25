package com.sprint.mission.discodeit.service.dto.binarycontent;

public record CreateBinaryContentRequest(
        byte[] data,
        String fileName,
        String contentType
) {
}
