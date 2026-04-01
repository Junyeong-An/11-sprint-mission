package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.CreateBinaryContentRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> findByQuery(@RequestParam UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.findEntity(binaryContentId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BinaryContentResponse find(@PathVariable UUID id) {
        return binaryContentService.find(id);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable UUID id) {
        BinaryContentDownloadResponse downloadResponse = binaryContentService.download(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(downloadResponse.fileName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.setContentType(resolveMediaType(downloadResponse.contentType()));

        return new ResponseEntity<>(downloadResponse.data(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAll(@RequestParam List<UUID> ids) {
        BinaryContentDownloadResponse downloadResponse = binaryContentService.downloadAll(ids);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(downloadResponse.fileName(), StandardCharsets.UTF_8)
                        .build()
        );
        headers.setContentType(resolveMediaType(downloadResponse.contentType()));

        return new ResponseEntity<>(downloadResponse.data(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponse> findAllByIdIn(@RequestParam(required = false) List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }

    @RequestMapping(method = RequestMethod.POST)
    public BinaryContentResponse create(@RequestBody CreateBinaryContentRequest request) {
        return binaryContentService.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        binaryContentService.delete(id);
    }

    private MediaType resolveMediaType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        try {
            return MediaType.parseMediaType(contentType);
        } catch (IllegalArgumentException exception) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
