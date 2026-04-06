package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResourceController {
    private final UserService userService;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAllUserDtos();
        return ResponseEntity.ok(users);
    }
}
