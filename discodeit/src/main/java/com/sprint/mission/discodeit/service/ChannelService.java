package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.service.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;

    @Transactional
    public ChannelResponse createPublicChannel(CreatePublicChannelRequest request) {
        validatePublicChannelRequest(request);
        Channel savedChannel = channelRepository.save(
                Channel.publicChannel(request.name(), request.description())
        );
        return toResponse(savedChannel);
    }

    @Transactional
    public ChannelResponse createPrivateChannel(CreatePrivateChannelRequest request) {
        validatePrivateChannelRequest(request);

        Channel savedChannel = channelRepository.save(Channel.privateChannel());

        // 참여자별 ReadStatus 생성
        request.participantIds().stream()
                .distinct()
                .forEach(participantId -> {
                    User user = userRepository.findById(participantId)
                            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
                    readStatusRepository.save(
                            new ReadStatus(user, savedChannel, savedChannel.getCreatedAt())
                    );
                });

        return toResponse(savedChannel);
    }

    public ChannelResponse find(UUID id) {
        return toResponse(getChannel(id));
    }

    public List<ChannelResponse> findAllByUserId(UUID userId) {
        if (userId == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

        List<UUID> visiblePrivateChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> readStatus.getChannel().getId())
                .distinct()
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> isVisibleChannel(channel, visiblePrivateChannelIds))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ChannelResponse update(UpdateChannelRequest request) {
        validateUpdateChannelRequest(request);

        Channel channel = getChannel(request.channelId());
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new DiscodeitException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
        }

        // 변경 감지로 자동 반영
        channel.update(request.name(), request.description());
        return toResponse(channel);
    }

    @Transactional
    public void delete(UUID id) {
        Channel channel = getChannel(id);

        // 채널 연관 메시지 삭제: Message 엔티티의 attachments는 CascadeType.ALL + orphanRemoval이므로
        // 엔티티를 로드하여 삭제하면 첨부파일까지 자동 삭제됨
        List<Message> messages = messageRepository.findAllByChannelId(id);
        messageRepository.deleteAll(messages);

        // 읽음상태도 삭제
        readStatusRepository.deleteAllByChannelId(id);

        // 채널 삭제
        channelRepository.delete(channel);
    }

    private Channel getChannel(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        return channelRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    private ChannelResponse toResponse(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<UserResponse> participants = channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser().getId())
                .distinct()
                .map(userService::find)
                .toList()
                : null;

        return ChannelResponse.builder()
                .id(channel.getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .type(channel.getType())
                .lastMessageAt(lastMessageAt)
                .participants(participants)
                .createdAt(channel.getCreatedAt())
                .updatedAt(channel.getUpdatedAt())
                .build();
    }

    private void validatePublicChannelRequest(CreatePublicChannelRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "공개 채널 생성 요청값이 비어있어요.");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "공개 채널 이름이 비어있어요.");
        }
    }

    private void validatePrivateChannelRequest(CreatePrivateChannelRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "비공개 채널 생성 요청값이 비어있어요.");
        }
        if (request.participantIds() == null || request.participantIds().isEmpty()) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "비공개 채널 참여자는 최소 1명 이상 필요해요.");
        }
    }

    private void validateUpdateChannelRequest(UpdateChannelRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "채널 수정 요청값이 비어있어요.");
        }
        if (request.channelId() == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "채널 이름이 비어있어요.");
        }
    }

    private boolean isVisibleChannel(Channel channel, List<UUID> visiblePrivateChannelIds) {
        if (channel.getType() == ChannelType.PUBLIC) {
            return true;
        }
        return visiblePrivateChannelIds.contains(channel.getId());
    }
}
