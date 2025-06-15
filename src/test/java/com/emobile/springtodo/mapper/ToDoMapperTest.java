package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ToDoMapperTest {

    private final ToDoMapper mapper = new ToDoMapperImpl();

    private ToDo todo;
    private ToDoRequestDto requestDto;
    private ToDoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        String id = "e487f331-5b7d-40b9-b49d-b41f94b2c960";
        todo = ToDo.builder()
                .id(UUID.fromString(id))
                .description("bla bla bla")
                .expirationDate(LocalDateTime.now().plusYears(1))
                .done(false)
                .build();

        responseDto = ToDoResponseDto.builder()
                .id(todo.getId())
                .description(todo.getDescription())
                .expirationDate(todo.getExpirationDate())
                .done(todo.isDone())
                .build();

        requestDto = ToDoRequestDto.builder()
                .description(todo.getDescription())
                .expirationDate(todo.getExpirationDate())
                .done(todo.isDone())
                .build();
    }

    @Test
    @DisplayName("should convert requestDto to Entity")
    void fromRequestToEntity() {
        todo.setId(null);

        ToDo actual = mapper.fromRequestToEntity(requestDto);

        assertThat(actual).isEqualTo(todo);
    }

    @Test
    @DisplayName("should convert Entity to responseDto")
    void fromEntityToResponseDto() {
        ToDoResponseDto actual = mapper.fromEntityToResponseDto(todo);

        assertThat(actual).isEqualTo(responseDto);
    }
}