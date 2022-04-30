package com.obstrom.todolistservice.dto;

import javax.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank String username,
        @NotBlank String password
) {
}
