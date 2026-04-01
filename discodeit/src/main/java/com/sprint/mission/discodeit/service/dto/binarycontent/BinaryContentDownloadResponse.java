package com.sprint.mission.discodeit.service.dto.binarycontent;

public record BinaryContentDownloadResponse(
        byte[] data,
        String fileName,
        String contentType
) {
}

