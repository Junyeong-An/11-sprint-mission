package com.sprint.mission.discodeit.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-statuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.POST)
    public ReadStatusResponse createByChannel(
            @PathVariable UUID channelId,
            @RequestBody CreateReadStatusByChannelRequest request
    ) {
        return readStatusService.createByChannel(channelId, request.userId());
    }

    @RequestMapping(value = "/channels/{channelId}/{readStatusId}", method = RequestMethod.PUT)
    public ReadStatusResponse updateByChannel(
            @PathVariable UUID channelId,
            @PathVariable UUID readStatusId,
            @RequestBody UpdateReadStatusByChannelRequest request
    ) {
        return readStatusService.updateByChannel(channelId, readStatusId, request.lastReadAt());
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public List<ReadStatusResponse> findAllByUser(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ReadStatusResponse find(@PathVariable UUID id) {
        return readStatusService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatusResponse> findAllByUserId(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusResponse create(@RequestBody CreateReadStatusRequest request) {
        return readStatusService.create(request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ReadStatusResponse update(@RequestBody UpdateReadStatusRequest request) {
        return readStatusService.update(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        readStatusService.delete(id);
    }
}
