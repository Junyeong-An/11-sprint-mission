package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {
    private final byte[] data;
    private final String fileName;
    private final String contentType;

    public BinaryContent() {
        super();
        this.data = new byte[0];
        this.fileName = "";
        this.contentType = "";
    }

    public BinaryContent(byte[] data, String fileName, String contentType) {
        super();
        this.data = data == null ? new byte[0] : data;
        this.fileName = fileName == null ? "" : fileName;
        this.contentType = contentType == null ? "" : contentType;
    }
}
