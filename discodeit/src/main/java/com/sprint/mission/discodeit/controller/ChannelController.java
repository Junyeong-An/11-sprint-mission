package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.UpdateChannelRequest;
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
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChannelResponse find(@PathVariable UUID id) {
        return channelService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelResponse> findAllByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ChannelResponse createPublic(@RequestBody CreatePublicChannelRequest request) {
        return channelService.createPublicChannel(request);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ChannelResponse createPrivate(@RequestBody CreatePrivateChannelRequest request) {
        return channelService.createPrivateChannel(request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ChannelResponse update(@RequestBody UpdateChannelRequest request) {
        return channelService.update(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        channelService.delete(id);
    }
}

