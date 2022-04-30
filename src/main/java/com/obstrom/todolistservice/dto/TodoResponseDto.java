package com.obstrom.todolistservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TodoResponseDto(
        String id,
        Date createdAt,
        String userId,
        String message,
        Boolean completed,
        Date completedAt
) {
}
