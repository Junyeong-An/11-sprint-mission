package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private BinaryContentStorage binaryContentStorage;

    @InjectMocks private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password1234")
                .profile(null)
                .build();

        userDto = UserDto.builder()
                .id(user.getId())
                .username("testuser")
                .email("test@example.com")
                .online(false)
                .build();
    }

    @Test
    void create_성공() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password1234")
                .build();

        given(userRepository.existsByUsername("testuser")).willReturn(false);
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toDto(user)).willReturn(userDto);

        UserDto result = userService.create(request);

        assertThat(result).isEqualTo(userDto);
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void create_중복된_username_예외() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password1234")
                .build();

        given(userRepository.existsByUsername("testuser")).willReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void create_중복된_email_예외() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password1234")
                .build();

        given(userRepository.existsByUsername("testuser")).willReturn(false);
        given(userRepository.existsByEmail("test@example.com")).willReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void update_성공() {
        UUID userId = user.getId();
        UpdateUserRequest request = UpdateUserRequest.builder()
                .userId(userId)
                .username("updateduser")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.findByUsername("updateduser")).willReturn(Optional.empty());
        given(userMapper.toDto(user)).willReturn(userDto);

        UserDto result = userService.update(request);

        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void update_사용자_없음_예외() {
        UUID userId = UUID.randomUUID();
        UpdateUserRequest request = UpdateUserRequest.builder()
                .userId(userId)
                .username("updateduser")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void delete_성공() {
        UUID userId = user.getId();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);

        then(userRepository).should().delete(user);
    }

    @Test
    void delete_사용자_없음_예외() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}
