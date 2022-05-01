package com.obstrom.todolistservice.dto;

import com.obstrom.todolistservice.security.UserRole;

import java.util.Date;

public record UserResponseDto(
        String id,
        Date createdAt,
        String username,
        UserRole userRole
) {
}
