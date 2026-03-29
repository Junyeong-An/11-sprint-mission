package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.service.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.dto.userstatus.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserResponse find(@PathVariable UUID id) {
        return userService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public UserResponse update(@RequestBody UpdateUserRequest request) {
        return userService.update(request);
    }

    @RequestMapping(value = "/{id}/online-status", method = RequestMethod.PUT)
    public UserStatusResponse updateOnlineStatus(@PathVariable UUID id) {
        return userStatusService.updateByUserId(
                new UpdateUserStatusByUserIdRequest(id, Instant.now())
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
