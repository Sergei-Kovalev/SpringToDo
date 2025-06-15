package com.emobile.springtodo.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Builder
public record ToDoRequestDto(
        @Length(min = 1, max = 255, message = "Description must contain from 1 to 255 characters")
        @NotBlank(message = "Description can't be blank")
        String description,

        @NotNull(message = "Expiration date can't be null")
        @FutureOrPresent(message = "Expiration date must be in future")
        LocalDateTime expirationDate,

        boolean done) {
}
