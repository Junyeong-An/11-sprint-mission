package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageResponse;
import com.sprint.mission.discodeit.service.dto.message.UpdateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MessageResponse find(@PathVariable UUID id) {
        return messageService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResponse> findAllByChannelId(@RequestParam UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public MessageResponse create(@RequestBody CreateMessageRequest request) {
        return messageService.create(request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public MessageResponse update(@RequestBody UpdateMessageRequest request) {
        return messageService.update(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        messageService.delete(id);
    }
}

