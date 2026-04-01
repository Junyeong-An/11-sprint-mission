package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusResponse;
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
@RequestMapping("/api/user-statuses")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserStatusResponse find(@PathVariable UUID id) {
        return userStatusService.find(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserStatusResponse> findAll() {
        return userStatusService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserStatusResponse create(@RequestBody CreateUserStatusRequest request) {
        return userStatusService.create(request);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public UserStatusResponse update(@RequestBody UpdateUserStatusRequest request) {
        return userStatusService.update(request);
    }

    @RequestMapping(value = "/by-user-id", method = RequestMethod.PUT)
    public UserStatusResponse updateByUserId(@RequestBody UpdateUserStatusByUserIdRequest request) {
        return userStatusService.updateByUserId(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userStatusService.delete(id);
    }
}

