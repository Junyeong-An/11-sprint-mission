package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public BinaryContentDto findByQuery(@RequestParam UUID binaryContentId) {
        return binaryContentService.find(binaryContentId);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public BinaryContentDto find(@PathVariable UUID binaryContentId) {
        return binaryContentService.find(binaryContentId);
    }

    @RequestMapping(value = "/{binaryContentId}/download", method = RequestMethod.GET)
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        BinaryContentDto dto = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(dto);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAll(@RequestParam List<UUID> ids) {
        return binaryContentService.downloadAll(ids);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentDto> findAllByIdIn(@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @RequestMapping(method = RequestMethod.POST)
    public BinaryContentDto create(@RequestBody CreateBinaryContentRequest request) {
        return binaryContentService.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        binaryContentService.delete(id);
    }
}
