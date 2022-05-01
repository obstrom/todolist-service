package com.obstrom.todolistservice.service;

import com.obstrom.todolistservice.dto.UserRequestDto;
import com.obstrom.todolistservice.dto.UserResponseDto;
import com.obstrom.todolistservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoService {

    private final UserService userService;

    public UserDtoService(UserService userService) {
        this.userService = userService;
    }

    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers().stream()
                .map(this::mapUserToResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto findUserById(String userId) {
        User user = userService.findUserById(userId);
        return mapUserToResponseDto(user);
    }

    public UserResponseDto findUserByUsername(String username) {
        User user = userService.findUserByUsername(username);
        return mapUserToResponseDto(user);
    }

    public UserResponseDto createNewUser(UserRequestDto userDto) {
        User user = userService.createNewUser(userDto.username(), userDto.password());
        return mapUserToResponseDto(user);
    }

    public UserResponseDto createNewAdminUser(UserRequestDto userDto) {
        User user = userService.createNewAdminUser(userDto.username(), userDto.password());
        return mapUserToResponseDto(user);
    }

    public void deleteUser(String userId) {
        userService.deleteUser(userId);
    }

    private UserResponseDto mapUserToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUsername(),
                user.getRole()
        );
    }

}
