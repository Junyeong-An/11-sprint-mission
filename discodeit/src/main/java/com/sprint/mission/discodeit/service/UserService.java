package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.controller.dto.UserUpdateApiRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.service.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UserProfileRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        validateCreateRequest(request);
        validateUniqueUsername(request.username());
        validateUniqueEmail(request.email());

        // 프로필 BinaryContent는 User의 cascade 설정으로 함께 저장됨
        BinaryContent profile = toBinaryContentFromProfile(request.profile());
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .profile(profile)
                .build();

        // UserStatus는 User의 cascade 설정으로 함께 저장됨
        UserStatus userStatus = new UserStatus(user);
        user.assignStatus(userStatus);

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional
    public UserResponse create(CreateUserRequest request, MultipartFile profile) {
        CreateUserRequest mergedRequest = new CreateUserRequest(
                request.username(),
                request.email(),
                request.password(),
                toUserProfileRequest(profile)
        );
        return create(mergedRequest);
    }

    public UserResponse find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        return toResponse(user);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UserDto> findAllUserDtos() {
        return findAll().stream()
                .map(this::toUserDto)
                .toList();
    }

    @Transactional
    public UserResponse update(UpdateUserRequest request) {
        if (request == null || request.userId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

        String updatedUsername = resolveUpdatedUsername(user, request);
        String updatedEmail = resolveUpdatedEmail(user, request);
        String updatedPassword = resolveUpdatedPassword(user, request);

        // 변경 감지(dirty checking) - 별도 save 호출 불필요
        user.update(updatedUsername, updatedEmail, updatedPassword);
        replaceProfileIfPresent(user, request.replacementProfile());

        return toResponse(user);
    }

    @Transactional
    public UserResponse update(UUID userId, UpdateUserRequest request, MultipartFile profile) {
        UpdateUserRequest mergedRequest = new UpdateUserRequest(
                userId,
                request.username(),
                request.email(),
                request.password(),
                toUserProfileRequest(profile)
        );
        return update(mergedRequest);
    }

    @Transactional
    public UserResponse update(UUID userId, UserUpdateApiRequest request, MultipartFile profile) {
        UpdateUserRequest convertedRequest = new UpdateUserRequest(
                userId,
                request.newUsername(),
                request.newEmail(),
                request.newPassword(),
                null
        );
        return update(userId, convertedRequest, profile);
    }

    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        // User의 cascade 설정으로 profile, userStatus가 함께 삭제됨
        userRepository.delete(user);
    }

    private void validateCreateRequest(CreateUserRequest request) {
        if (request == null) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "요청값이 비어있어요.");
        }
        if (isBlank(request.username())) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        if (isBlank(request.email())) {
            throw new DiscodeitException(ErrorCode.EMAIL_REQUIRED);
        }
        if (request.password() == null) {
            throw new DiscodeitException(ErrorCode.PASSWORD_REQUIRED);
        }
    }

    private BinaryContent toBinaryContentFromProfile(UserProfileRequest profile) {
        if (profile == null) {
            return null;
        }
        // User의 cascade로 함께 저장되므로 별도로 binaryContentRepository.save() 불필요
        return new BinaryContent(
                profile.data(),
                profile.fileName(),
                profile.contentType()
        );
    }

    /**
     * 프로필 교체 로직.
     * orphanRemoval = true 설정으로 이전 profile은 참조가 끊기는 순간 자동 삭제된다.
     */
    private void replaceProfileIfPresent(User user, UserProfileRequest profile) {
        if (profile == null) {
            return;
        }
        BinaryContent newProfile = toBinaryContentFromProfile(profile);
        user.replaceProfile(newProfile);
    }

    private void validateUniqueUsername(UUID userId, String username) {
        if (isBlank(username)) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        userRepository.findByUsername(username)
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .ifPresent(user -> {
                    throw new DiscodeitException(ErrorCode.DUPLICATE_USERNAME);
                });
    }

    private void validateUniqueUsername(String username) {
        if (isBlank(username)) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        if (userRepository.existsByUsername(username)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    private void validateUniqueEmail(String email) {
        if (isBlank(email)) {
            throw new DiscodeitException(ErrorCode.EMAIL_REQUIRED);
        }
        if (userRepository.existsByEmail(email)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateUniqueEmail(UUID userId, String email) {
        if (isBlank(email)) {
            throw new DiscodeitException(ErrorCode.EMAIL_REQUIRED);
        }
        userRepository.findByEmail(email)
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .ifPresent(user -> {
                    throw new DiscodeitException(ErrorCode.DUPLICATE_EMAIL);
                });
    }

    private String resolveUpdatedUsername(User user, UpdateUserRequest request) {
        if (request.username() == null) {
            return user.getUsername();
        }
        validateUniqueUsername(request.userId(), request.username());
        return request.username();
    }

    private String resolveUpdatedEmail(User user, UpdateUserRequest request) {
        if (request.email() == null) {
            return user.getEmail();
        }
        validateUniqueEmail(request.userId(), request.email());
        return request.email();
    }

    private String resolveUpdatedPassword(User user, UpdateUserRequest request) {
        if (isBlank(request.password())) {
            return user.getPassword();
        }
        return request.password();
    }

    private UserResponse toResponse(User user) {
        // 지연 로딩: status/profile 접근 시점에 프록시 초기화
        boolean online = user.getStatus() != null && user.getStatus().isOnline();
        BinaryContentResponse profile = user.getProfile() != null
                ? toBinaryContentResponse(user.getProfile())
                : null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(profile)
                .online(online)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    BinaryContentResponse toBinaryContentResponse(BinaryContent binaryContent) {
        return BinaryContentResponse.builder()
                .id(binaryContent.getId())
                .createdAt(binaryContent.getCreatedAt())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getSize())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getBytes())
                .build();
    }

    private UserDto toUserDto(UserResponse userResponse) {
        UUID profileId = userResponse.profile() != null ? userResponse.profile().id() : null;
        return new UserDto(
                userResponse.id(),
                userResponse.createdAt(),
                userResponse.updatedAt(),
                userResponse.username(),
                userResponse.email(),
                profileId,
                userResponse.online()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private UserProfileRequest toUserProfileRequest(MultipartFile profile) {
        if (profile == null || profile.isEmpty()) {
            return null;
        }
        try {
            return new UserProfileRequest(
                    profile.getBytes(),
                    profile.getOriginalFilename(),
                    profile.getContentType()
            );
        } catch (IOException exception) {
            throw new DiscodeitException(ErrorCode.INVALID_REQUEST, "프로필 파일을 읽을 수 없어요.");
        }
    }
}
