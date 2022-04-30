package com.obstrom.todolistservice.controller;

import com.obstrom.todolistservice.dto.UserRequestDto;
import com.obstrom.todolistservice.dto.UserResponseDto;
import com.obstrom.todolistservice.model.User;
import com.obstrom.todolistservice.service.UserDtoService;
import com.obstrom.todolistservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserRestController {

    private final UserDtoService userDtoService;

    public UserRestController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userDtos = userDtoService.findAllUsers();

        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId) {
        UserResponseDto userDto = userDtoService.findUserById(userId);

        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping(path = "{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto userDto = userDtoService.findUserByUsername(username);

        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> postNewUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto userDto = userDtoService.createNewUser(requestDto);

        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable String userId) {
        userDtoService.deleteUser(userId);
    }

}
