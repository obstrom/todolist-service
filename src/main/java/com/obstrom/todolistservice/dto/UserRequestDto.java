package com.obstrom.todolistservice.dto;

public record UserRequestDto(
        String username,
        String password
) {
}
