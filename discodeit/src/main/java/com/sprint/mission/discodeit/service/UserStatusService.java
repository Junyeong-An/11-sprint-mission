package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatusResponse create(CreateUserStatusRequest request) {
        validateCreateRequest(request);
        ensureUserExists(request.userId());

        if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_USER_STATUS);
        }

        UserStatus userStatus = new UserStatus(request.userId());
        return toResponse(userStatusRepository.save(userStatus));
    }

    public UserStatusResponse find(UUID id) {
        return toResponse(getUserStatus(id));
    }

    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserStatusResponse update(UpdateUserStatusRequest request) {
        validateUpdateRequest(request);

        UserStatus userStatus = getUserStatus(request.userStatusId());
        userStatus.updateLastConnectedAt(request.lastConnectedAt());
        return toResponse(userStatusRepository.save(userStatus));
    }

    public UserStatusResponse updateByUserId(UpdateUserStatusByUserIdRequest request) {
        validateUpdateByUserIdRequest(request);
        ensureUserExists(request.userId());

        UserStatus userStatus = userStatusRepository.findByUserId(request.userId())
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.updateLastConnectedAt(request.lastConnectedAt());
        return toResponse(userStatusRepository.save(userStatus));
    }

    public UserStatusResponse updateByUserId(UUID userId, Instant lastConnectedAt) {
        Instant resolvedLastConnectedAt = lastConnectedAt != null ? lastConnectedAt : Instant.now();
        return updateByUserId(new UpdateUserStatusByUserIdRequest(userId, resolvedLastConnectedAt));
    }

    public void delete(UUID id) {
        getUserStatus(id);
        userStatusRepository.deleteById(id);
    }

    private UserStatus getUserStatus(UUID id) {
        if (id == null) {
            throw new DiscodeitException(ErrorCode.USER_STATUS_ID_REQUIRED);
        }
        return userStatusRepository.findById(id);
    }

    private void ensureUserExists(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
    }

    private UserStatusResponse toResponse(UserStatus userStatus) {
        return UserStatusResponse.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUserId())
                .lastActiveAt(userStatus.getLastConnectedAt())
                .online(userStatus.isOnline())
                .createdAt(userStatus.getCreatedAt())
                .updatedAt(userStatus.getUpdatedAt())
                .build();
    }

    private void validateCreateRequest(CreateUserStatusRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "유저상태 생성 요청값이 비어있어요.");
        }
        if (request.userId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
    }

    private void validateUpdateRequest(UpdateUserStatusRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "유저상태 수정 요청값이 비어있어요.");
        }
        if (request.userStatusId() == null) {
            throw new DiscodeitException(ErrorCode.USER_STATUS_ID_REQUIRED);
        }
        if (request.lastConnectedAt() == null) {
            throw new DiscodeitException(ErrorCode.LAST_CONNECTED_AT_REQUIRED);
        }
    }

    private void validateUpdateByUserIdRequest(UpdateUserStatusByUserIdRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "유저상태 수정 요청값이 비어있어요.");
        }
        if (request.userId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }
        if (request.lastConnectedAt() == null) {
            throw new DiscodeitException(ErrorCode.LAST_CONNECTED_AT_REQUIRED);
        }
    }
}
