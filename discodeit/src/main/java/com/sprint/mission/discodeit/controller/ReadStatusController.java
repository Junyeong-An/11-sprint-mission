package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.controller.dto.ReadStatusUpdateApiRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.CreateReadStatusByChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.UpdateReadStatusByChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.UpdateReadStatusRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.POST)
    public ApiResponse<ReadStatusResponse> createByChannel(
            @PathVariable UUID channelId,
            @RequestBody CreateReadStatusByChannelRequest request
    ) {
        return ApiResponse.success(readStatusService.createByChannel(channelId, request.userId()));
    }

    @RequestMapping(value = "/{readStatusId}/channels/{channelId}", method = RequestMethod.PUT)
    public ApiResponse<ReadStatusResponse> updateByChannel(
            @PathVariable UUID channelId,
            @PathVariable UUID readStatusId,
            @RequestBody UpdateReadStatusByChannelRequest request
    ) {
        return ApiResponse.success(readStatusService.updateByChannel(channelId, readStatusId, request.lastReadAt()));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ApiResponse<List<ReadStatusResponse>> findAllByUser(@PathVariable UUID userId) {
        return ApiResponse.success(readStatusService.findAllByUserId(userId));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<ReadStatusResponse> find(@PathVariable UUID id) {
        return ApiResponse.success(readStatusService.find(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReadStatusResponse> create(@RequestBody CreateReadStatusRequest request) {
        return ApiResponse.success(readStatusService.create(request));
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ApiResponse<ReadStatusResponse> update(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateApiRequest request
    ) {
        return ApiResponse.success(readStatusService.update(new UpdateReadStatusRequest(
                readStatusId,
                request.newLastReadAt()
        )));
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.DELETE)
    public ApiResponse<Void> delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ApiResponse.success();
    }
}
