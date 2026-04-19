package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.ReadStatusUpdateApiRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.CreateReadStatusByChannelRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusDto;
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
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/channels/{channelId}", method = RequestMethod.POST)
    public ReadStatusDto createByChannel(
            @PathVariable UUID channelId,
            @RequestBody CreateReadStatusByChannelRequest request
    ) {
        return readStatusService.createByChannel(channelId, request.userId());
    }

    @RequestMapping(value = "/{readStatusId}/channels/{channelId}", method = RequestMethod.PUT)
    public ReadStatusDto updateByChannel(
            @PathVariable UUID channelId,
            @PathVariable UUID readStatusId,
            @RequestBody UpdateReadStatusByChannelRequest request
    ) {
        return readStatusService.updateByChannel(channelId, readStatusId, request.lastReadAt());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatusDto> findAllByUser(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ReadStatusDto find(@PathVariable UUID id) {
        return readStatusService.find(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ReadStatusDto create(@RequestBody CreateReadStatusRequest request) {
        return readStatusService.create(request);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ReadStatusDto update(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateApiRequest request
    ) {
        return readStatusService.update(new UpdateReadStatusRequest(
                readStatusId,
                request.newLastReadAt()
        ));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);
    }
}
