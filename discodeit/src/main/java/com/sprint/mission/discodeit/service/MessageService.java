package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageAttachmentRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageResponse;
import com.sprint.mission.discodeit.service.dto.message.UpdateMessageRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    public MessageResponse create(CreateMessageRequest request) {
        validateCreateRequest(request);
        userRepository.findById(request.authorId())
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        getChannel(request.channelId());

        List<UUID> attachmentIds = saveAttachments(request.attachments());
        Message message = new Message(
                request.authorId(),
                request.channelId(),
                request.content(),
                attachmentIds
        );

        return toResponse(messageRepository.save(message));
    }

    public MessageResponse create(CreateMessageRequest request, List<MultipartFile> attachments) {
        CreateMessageRequest mergedRequest = new CreateMessageRequest(
                request.authorId(),
                request.channelId(),
                request.content(),
                toAttachmentRequests(attachments)
        );
        return create(mergedRequest);
    }

    public MessageResponse find(UUID id) {
        return toResponse(getMessage(id));
    }

    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        getChannel(channelId);

        return messageRepository.findAllByChannelId(channelId).stream()
                .map(this::toResponse)
                .toList();
    }

    public MessageResponse update(UpdateMessageRequest request) {
        validateUpdateRequest(request);
        Message message = getMessage(request.messageId());
        message.update(request.content());
        return toResponse(messageRepository.save(message));
    }

    public void delete(UUID id) {
        Message message = getMessage(id);

        message.getAttachmentIds().forEach(binaryContentRepository::deleteById);
        messageRepository.deleteById(id);
    }

    private Message getMessage(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.MESSAGE_ID_REQUIRED);
        }
        return messageRepository.findById(id);
    }

    private void getChannel(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        channelRepository.findById(id);
    }

    private List<UUID> saveAttachments(List<MessageAttachmentRequest> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return List.of();
        }

        return attachments.stream()
                .map(this::saveAttachment)
                .toList();
    }

    private UUID saveAttachment(MessageAttachmentRequest attachmentRequest) {
        if (attachmentRequest == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "첨부파일 정보가 비어있어요.");
        }
        BinaryContent binaryContent = new BinaryContent(
                attachmentRequest.data(),
                attachmentRequest.fileName(),
                attachmentRequest.contentType()
        );
        return binaryContentRepository.save(binaryContent).getId();
    }

    private List<MessageAttachmentRequest> toAttachmentRequests(List<MultipartFile> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return List.of();
        }

        List<MessageAttachmentRequest> requests = new ArrayList<>();
        for (MultipartFile attachment : attachments) {
            if (attachment == null || attachment.isEmpty()) {
                continue;
            }
            try {
                requests.add(new MessageAttachmentRequest(
                        attachment.getBytes(),
                        attachment.getOriginalFilename(),
                        attachment.getContentType()
                ));
            } catch (IOException exception) {
                throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "첨부파일을 읽을 수 없어요.");
            }
        }
        return requests;
    }

    private MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .authorId(message.getAuthorId())
                .channelId(message.getChannelId())
                .content(message.getContent())
                .attachmentIds(List.copyOf(message.getAttachmentIds()))
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    private void validateCreateRequest(CreateMessageRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "메시지 생성 요청값이 비어있어요.");
        }
        if (request.authorId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
        if (request.channelId() == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new DiscodeitException(ErrorCode.MESSAGE_CONTENT_REQUIRED);
        }
    }

    private void validateUpdateRequest(UpdateMessageRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "메시지 수정 요청값이 비어있어요.");
        }
        if (request.messageId() == null) {
            throw new DiscodeitException(ErrorCode.MESSAGE_ID_REQUIRED);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new DiscodeitException(ErrorCode.MESSAGE_CONTENT_REQUIRED);
        }
    }
}
