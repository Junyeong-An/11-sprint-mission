package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final String fileName;
    private final long size;
    private final String contentType;
    private final byte[] bytes;

    public BinaryContent(byte[] bytes, String fileName, String contentType) {
        super();
        this.bytes = bytes == null ? new byte[0] : bytes;
        this.size = this.bytes.length;
        this.fileName = fileName == null ? "" : fileName;
        this.contentType = contentType == null ? "" : contentType;
    }
}
