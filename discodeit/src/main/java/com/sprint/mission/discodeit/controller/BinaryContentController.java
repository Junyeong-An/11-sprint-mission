package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.binarycontent.CreateBinaryContentRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ApiResponse<BinaryContentResponse> findByQuery(@RequestParam UUID binaryContentId) {
        return ApiResponse.success(binaryContentService.find(binaryContentId));
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ApiResponse<BinaryContentResponse> find(@PathVariable UUID binaryContentId) {
        return ApiResponse.success(binaryContentService.find(binaryContentId));
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ApiResponse<BinaryContentDownloadResponse> download(@PathVariable UUID id) {
        return ApiResponse.success(binaryContentService.download(id));
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ApiResponse<BinaryContentDownloadResponse> downloadAll(@RequestParam List<UUID> ids) {
        return ApiResponse.success(binaryContentService.downloadAll(ids));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<BinaryContentResponse>> findAllByIdIn(@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return ApiResponse.success(binaryContentService.findAllByIdIn(binaryContentIds));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<BinaryContentResponse> create(@RequestBody CreateBinaryContentRequest request) {
        return ApiResponse.success(binaryContentService.create(request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        binaryContentService.delete(id);
    }
}
