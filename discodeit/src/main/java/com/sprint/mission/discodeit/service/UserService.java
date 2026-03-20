package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UserProfileRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public UserResponse find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요."));
        return toResponse(user);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요."));
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    public UserResponse create(CreateUserRequest request) {
        validateCreateRequest(request);
        validateUniqueUsername(request.username());
        validateUniqueEmail(request.email());

        UUID profileId = saveProfileIfPresent(request.profile());
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .profileId(profileId)
                .build();
        User savedUser = userRepository.save(user);
        userStatusRepository.save(new UserStatus(savedUser.getId()));
        return toResponse(savedUser, false);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        Map<UUID, UserStatus> statusByUserId = loadStatusMap(users);
        return users.stream()
                .map(user -> toResponse(user, statusByUserId))
                .toList();
    }

    public UserResponse update(UpdateUserRequest request) {
        if (request == null || request.userId() == null) {
            throw new IllegalArgumentException("유저ID가 비어있어요.");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요."));
        validateUniqueUsername(request.userId(), request.username());
        validateUniqueEmail(request.userId(), request.email());

        user.update(request.username(), request.email(), request.password());
        replaceProfileIfPresent(user, request.replacementProfile());
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    private void validateCreateRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("요청값이 비어있어요.");
        }
        if (isBlank(request.username())) {
            throw new IllegalArgumentException("유저이름이 비어있어요.");
        }
        if (isBlank(request.email())) {
            throw new IllegalArgumentException("이메일이 비어있어요.");
        }
        if (request.password() == null) {
            throw new IllegalArgumentException("비밀번호가 비어있어요.");
        }
    }

    private UUID saveProfileIfPresent(UserProfileRequest profile) {
        if (profile == null) {
            return null;
        }

        BinaryContent binaryContent = new BinaryContent(
                profile.data(),
                profile.fileName(),
                profile.contentType()
        );
        BinaryContent saved = binaryContentRepository.save(binaryContent);
        return saved.getId();
    }

    private void replaceProfileIfPresent(User user, UserProfileRequest profile) {
        if (profile == null) {
            return;
        }

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        UUID newProfileId = saveProfileIfPresent(profile);
        user.replaceProfile(newProfileId);
    }

    private void validateUniqueUsername(UUID userId, String username) {
        if (isBlank(username)) {
            throw new IllegalArgumentException("유저이름이 비어있어요.");
        }

        userRepository.findByUserName(username)
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("해당 유저이름이 이미 존재해요.");
                });
    }

    private void validateUniqueUsername(String username) {
        userRepository.findByUserName(username)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("해당 유저이름이 이미 존재해요.");
                });
    }

    private void validateUniqueEmail(String email) {
        if (isBlank(email)) {
            throw new IllegalArgumentException("이메일이 비어있어요.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("해당 이메일이 이미 존재해요.");
        }
    }

    private void validateUniqueEmail(UUID userId, String email) {
        if (isBlank(email)) {
            throw new IllegalArgumentException("이메일이 비어있어요.");
        }

        userRepository.findByEmail(email)
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("해당 이메일이 이미 존재해요.");
                });
    }

    private Map<UUID, UserStatus> loadStatusMap(List<User> users) {
        List<UUID> userIds = users.stream()
                .map(User::getId)
                .toList();

        return userStatusRepository.findByUserIdIn(userIds).stream()
                .collect(Collectors.toMap(UserStatus::getUserId, Function.identity()));
    }

    private UserResponse toResponse(User user) {
        boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);
        return toResponse(user, online);
    }

    private UserResponse toResponse(User user, Map<UUID, UserStatus> statusByUserId) {
        boolean online = Optional.ofNullable(statusByUserId.get(user.getId()))
                .map(UserStatus::isOnline)
                .orElse(false);
        return toResponse(user, online);
    }

    private UserResponse toResponse(User user, boolean online) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .online(online)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
