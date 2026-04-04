package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResourceController {
    private final UserService userService;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ApiResponse<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll().stream()
                .map(this::toDto)
                .toList();
        return ApiResponse.success(users);
    }

    private UserDto toDto(UserResponse userResponse) {
        return new UserDto(
                userResponse.id(),
                userResponse.createdAt(),
                userResponse.updatedAt(),
                userResponse.username(),
                userResponse.email(),
                userResponse.profileId(),
                userResponse.online()
        );
    }
}
