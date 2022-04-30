package com.obstrom.todolistservice.dto;

import java.util.Date;

public record UserResponseDto(
        String id,
        Date createdAt,
        String username
) {
}
