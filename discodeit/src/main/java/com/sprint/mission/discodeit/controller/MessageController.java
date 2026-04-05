package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.MessageUpdateApiRequest;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageResponse;
import com.sprint.mission.discodeit.service.dto.message.UpdateMessageRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageResponse create(
            @RequestPart("messageCreateRequest") CreateMessageRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        return messageService.create(messageCreateRequest, attachments);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public MessageResponse update(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateApiRequest request
    ) {
        return messageService.update(new UpdateMessageRequest(messageId, request.newContent()));
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }
}
