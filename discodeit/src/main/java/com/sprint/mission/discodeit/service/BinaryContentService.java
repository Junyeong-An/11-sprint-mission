package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.CreateBinaryContentRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContentResponse create(CreateBinaryContentRequest request) {
        validateCreateRequest(request);

        BinaryContent binaryContent = new BinaryContent(
                request.data(),
                request.fileName(),
                request.contentType()
        );
        return toResponse(binaryContentRepository.save(binaryContent));
    }

    public BinaryContentResponse find(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_ID_REQUIRED);
        }
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
        return toResponse(binaryContent);
    }

    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        Map<UUID, BinaryContent> binaryContentById = binaryContentRepository.findAll().stream()
                .collect(Collectors.toMap(BinaryContent::getId, Function.identity()));

        return ids.stream()
                .distinct()
                .map(binaryContentById::get)
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .toList();
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_ID_REQUIRED);
        }
        binaryContentRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
        binaryContentRepository.deleteById(id);
    }

    private BinaryContentResponse toResponse(BinaryContent binaryContent) {
        return BinaryContentResponse.builder()
                .id(binaryContent.getId())
                .createdAt(binaryContent.getCreatedAt())
                .data(binaryContent.getData())
                .fileName(binaryContent.getFileName())
                .contentType(binaryContent.getContentType())
                .build();
    }

    private void validateCreateRequest(CreateBinaryContentRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "첨부파일 생성 요청값이 비어있어요.");
        }
    }
}
