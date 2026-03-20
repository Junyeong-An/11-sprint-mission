package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent{
    private final UUID id;
    private final Instant createdAt;
    private final byte[] data;
    private final String fileName;
    private final String contentType;

    public BinaryContent() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = new byte[0];
        this.fileName = "";
        this.contentType = "";
    }

    public BinaryContent(byte[] data, String fileName, String contentType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data == null ? new byte[0] : data;
        this.fileName = fileName == null ? "" : fileName;
        this.contentType = contentType == null ? "" : contentType;
    }
}
