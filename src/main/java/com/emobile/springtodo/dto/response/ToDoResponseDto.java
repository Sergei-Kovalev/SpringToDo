package com.emobile.springtodo.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ToDoResponseDto(
        UUID id,
        String description,
        LocalDateTime expirationDate,
        boolean done) {
}
