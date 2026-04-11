package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.CreateBinaryContentRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
        BinaryContent binaryContent = findEntity(id);
        return toResponse(binaryContent);
    }

    public BinaryContent findEntity(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_ID_REQUIRED);
        }
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
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

    public BinaryContentDownloadResponse download(UUID id) {
        BinaryContentResponse binaryContent = find(id);
        return new BinaryContentDownloadResponse(
                binaryContent.bytes(),
                resolveFileName(binaryContent.fileName(), id),
                resolveContentType(binaryContent.contentType())
        );
    }

    public BinaryContentDownloadResponse downloadAll(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "다운로드할 첨부파일 ID 목록이 비어있어요.");
        }

        List<BinaryContentResponse> binaryContents = findAllByIdIn(ids);
        if (binaryContents.isEmpty()) {
            throw new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND);
        }

        return new BinaryContentDownloadResponse(
                toZipBytes(binaryContents),
                "binary-contents.zip",
                "application/zip"
        );
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
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getSize())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getBytes())
                .build();
    }

    private void validateCreateRequest(CreateBinaryContentRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "첨부파일 생성 요청값이 비어있어요.");
        }
    }

    private String resolveContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "application/octet-stream";
        }
        return contentType;
    }

    private String resolveFileName(String fileName, UUID fallbackId) {
        if (fileName == null || fileName.isBlank()) {
            return fallbackId + ".bin";
        }
        return fileName;
    }

    private byte[] toZipBytes(List<BinaryContentResponse> binaryContents) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {
            Map<String, Integer> fileNameCounter = new HashMap<>();
            for (BinaryContentResponse binaryContent : binaryContents) {
                String baseName = resolveFileName(binaryContent.fileName(), binaryContent.id());
                String entryName = resolveUniqueFileName(baseName, fileNameCounter);
                zos.putNextEntry(new ZipEntry(entryName));
                zos.write(binaryContent.bytes());
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (IOException exception) {
            throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, "바이너리 파일 압축 중 오류가 발생했어요.");
        }
    }

    private String resolveUniqueFileName(String fileName, Map<String, Integer> fileNameCounter) {
        int count = fileNameCounter.getOrDefault(fileName, 0);
        fileNameCounter.put(fileName, count + 1);
        if (count == 0) {
            return fileName;
        }
        return "(" + count + ")_" + fileName;
    }
}
