package com.sprint.mission.discodeit.service.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record PageResponse<T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        long totalElements
) {
}
