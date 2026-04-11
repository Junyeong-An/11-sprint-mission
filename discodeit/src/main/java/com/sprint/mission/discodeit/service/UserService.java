package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.controller.dto.UserUpdateApiRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public UserResponse find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        return toResponse(user);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));
        if (user.getProfile() != null) {
            binaryContentRepository.deleteById(user.getProfile().getId());
        }
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public UserResponse create(CreateUserRequest request) {
        validateCreateRequest(request);
        validateUniqueUsername(request.username());
        validateUniqueEmail(request.email());

        BinaryContent profile = saveProfileIfPresent(request.profile());
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .profile(profile)
                .build();
        User savedUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(savedUser);
        userStatusRepository.save(userStatus);
        savedUser.assignStatus(userStatus);
        userRepository.save(savedUser);

        return toResponse(savedUser);
    }

    public UserResponse create(CreateUserRequest request, MultipartFile profile) {
        CreateUserRequest mergedRequest = new CreateUserRequest(
                request.username(),
                request.email(),
                request.password(),
                toUserProfileRequest(profile)
        );
        return create(mergedRequest);
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

    public UserResponse update(UpdateUserRequest request) {
        if (request == null || request.userId() == null) {
            throw new DiscodeitException(ErrorCode.USER_ID_REQUIRED);
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND));

        String updatedUsername = resolveUpdatedUsername(user, request);
        String updatedEmail = resolveUpdatedEmail(user, request);
        String updatedPassword = resolveUpdatedPassword(user, request);

        user.update(updatedUsername, updatedEmail, updatedPassword);
        replaceProfileIfPresent(user, request.replacementProfile());
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

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

    private BinaryContent saveProfileIfPresent(UserProfileRequest profile) {
        if (profile == null) {
            return null;
        }
        BinaryContent binaryContent = new BinaryContent(
                profile.data(),
                profile.fileName(),
                profile.contentType()
        );
        return binaryContentRepository.save(binaryContent);
    }

    private void replaceProfileIfPresent(User user, UserProfileRequest profile) {
        if (profile == null) {
            return;
        }
        if (user.getProfile() != null) {
            binaryContentRepository.deleteById(user.getProfile().getId());
        }
        BinaryContent newProfile = saveProfileIfPresent(profile);
        user.replaceProfile(newProfile);
    }

    private void validateUniqueUsername(UUID userId, String username) {
        if (isBlank(username)) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        userRepository.findByUserName(username)
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .ifPresent(user -> {
                    throw new DiscodeitException(ErrorCode.DUPLICATE_USERNAME);
                });
    }

    private void validateUniqueUsername(String username) {
        if (isBlank(username)) {
            throw new DiscodeitException(ErrorCode.USERNAME_REQUIRED);
        }
        userRepository.findByUserName(username)
                .ifPresent(user -> {
                    throw new DiscodeitException(ErrorCode.DUPLICATE_USERNAME);
                });
    }

    private void validateUniqueEmail(String email) {
        if (isBlank(email)) {
            throw new DiscodeitException(ErrorCode.EMAIL_REQUIRED);
        }
        if (userRepository.findByEmail(email).isPresent()) {
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
