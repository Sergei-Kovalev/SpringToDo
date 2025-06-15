package com.emobile.springtodo.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ToDoResponseDto(
        UUID id,
        String description,
        LocalDateTime expirationDate,
        boolean isDone) {
}
