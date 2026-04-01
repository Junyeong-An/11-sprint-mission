package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.UpdateReadStatusRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponse create(CreateReadStatusRequest request) {
        validateCreateRequest(request);
        ensureUserExists(request.userId());
        ensureChannelExists(request.channelId());

        boolean duplicated = readStatusRepository.findByUserId(request.userId()).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(request.channelId()));
        if (duplicated) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_READ_STATUS);
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        return toResponse(readStatusRepository.save(readStatus));
    }

    public ReadStatusResponse createByChannel(UUID channelId, UUID userId) {
        return create(new CreateReadStatusRequest(userId, channelId));
    }

    public ReadStatusResponse find(UUID id) {
        return toResponse(getReadStatus(id));
    }

    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        if (userId == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
        ensureUserExists(userId);

        return readStatusRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ReadStatusResponse update(UpdateReadStatusRequest request) {
        validateUpdateRequest(request);

        ReadStatus readStatus = getReadStatus(request.readStatusId());
        readStatus.updateLastReadAt(request.lastReadAt());
        return toResponse(readStatusRepository.save(readStatus));
    }

    public ReadStatusResponse updateByChannel(UUID channelId, UUID readStatusId, Instant lastReadAt) {
        ReadStatus readStatus = getReadStatus(readStatusId);
        if (!readStatus.getChannelId().equals(channelId)) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "해당 채널의 메시지 수신 정보가 아니에요.");
        }
        return update(new UpdateReadStatusRequest(readStatusId, lastReadAt));
    }

    public void delete(UUID id) {
        getReadStatus(id);
        readStatusRepository.deleteById(id);
    }

    private ReadStatus getReadStatus(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.READ_STATUS_ID_REQUIRED);
        }
        return readStatusRepository.findById(id);
    }

    private void ensureUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
    }

    private void ensureChannelExists(UUID channelId) {
        if (channelId == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
        channelRepository.findById(channelId);
    }

    private ReadStatusResponse toResponse(ReadStatus readStatus) {
        return ReadStatusResponse.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .lastReadAt(readStatus.getLastReadAt())
                .createdAt(readStatus.getCreatedAt())
                .updatedAt(readStatus.getUpdatedAt())
                .build();
    }

    private void validateCreateRequest(CreateReadStatusRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "읽음상태 생성 요청값이 비어있어요.");
        }
        if (request.userId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
        if (request.channelId() == null) {
            throw new DiscodeitException(ErrorCode.CHANNEL_ID_REQUIRED);
        }
    }

    private void validateUpdateRequest(UpdateReadStatusRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "읽음상태 수정 요청값이 비어있어요.");
        }
        if (request.readStatusId() == null) {
            throw new DiscodeitException(ErrorCode.READ_STATUS_ID_REQUIRED);
        }
        if (request.lastReadAt() == null) {
            throw new DiscodeitException(ErrorCode.LAST_READ_AT_REQUIRED);
        }
    }
}
