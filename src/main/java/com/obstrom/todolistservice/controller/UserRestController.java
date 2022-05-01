package com.obstrom.todolistservice.controller;

import com.obstrom.todolistservice.dto.UserRequestDto;
import com.obstrom.todolistservice.dto.UserResponseDto;
import com.obstrom.todolistservice.service.UserDtoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/user")
public class UserRestController {

    private final UserDtoService userDtoService;

    public UserRestController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userDtos = userDtoService.findAllUsers();

        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(path = "{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable @NotBlank String userId) {
        UserResponseDto userDto = userDtoService.findUserById(userId);

        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping(path = "username/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable @NotBlank String username) {
        UserResponseDto userDto = userDtoService.findUserByUsername(username);

        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping(path = "self")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getThisAuthenticatedUser(Principal principal) {
        UserResponseDto userDto = userDtoService.findUserByUsername(principal.getName());

        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> postNewUser(@RequestBody @Valid UserRequestDto requestDto) {
        UserResponseDto userDto = userDtoService.createNewUser(requestDto);

        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping(path = "admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> postNewAdminUser(@RequestBody @Valid UserRequestDto requestDto) {
        UserResponseDto userDto = userDtoService.createNewAdminUser(requestDto);

        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping(path = "{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable @NotBlank String userId) {
        userDtoService.deleteUser(userId);
    }

}
