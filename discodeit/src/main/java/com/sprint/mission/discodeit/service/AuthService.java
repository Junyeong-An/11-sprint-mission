package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.user.UserLoginRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    public UserResponse login(UserLoginRequest request) {
        validateLoginRequest(request);
        User user = userRepository.findByUserName(request.username()).orElseThrow(
                () -> new DiscodeitException(ErrorCode.LOGIN_USER_NOT_FOUND));
        if (!user.getPassword().equals(request.password())) {
            throw new DiscodeitException(ErrorCode.INVALID_CREDENTIALS);
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserStatus(user.getId()));
        userStatus.updateLastConnectedAt();
        userStatusRepository.save(userStatus);

        BinaryContentResponse profile = null;
        if (user.getProfileId() != null) {
            profile = binaryContentRepository.findById(user.getProfileId())
                    .map(this::toBinaryContentResponse)
                    .orElse(null);
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(profile)
                .online(userStatus.isOnline())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private BinaryContentResponse toBinaryContentResponse(BinaryContent binaryContent) {
        return BinaryContentResponse.builder()
                .id(binaryContent.getId())
                .createdAt(binaryContent.getCreatedAt())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getData().length)
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getData())
                .build();
    }

    private void validateLoginRequest(UserLoginRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.LOGIN_REQUEST_REQUIRED);
        }
        if (request.username() == null || request.username().isBlank()) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new DiscodeitException(ErrorCode.PASSWORD_REQUIRED);
        }
    }
}
