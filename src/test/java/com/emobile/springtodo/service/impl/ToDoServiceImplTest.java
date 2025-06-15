package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {
    @Mock
    private ToDoRepository repository;
    @Mock
    private ToDoMapper mapper;
    @InjectMocks
    private ToDoServiceImpl service;

    private String id;
    private ToDo todo;
    private ToDoResponseDto responseDto;
    private ToDoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        id = "e487f331-5b7d-40b9-b49d-b41f94b2c960";
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
    @DisplayName("should find ToDo by ID when present")
    void findById_whenPresent() {
        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(todo));
        when(mapper.fromEntityToResponseDto(todo))
                .thenReturn(responseDto);

        ToDoResponseDto actual = service.findById(id);

        assertThat(actual).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("should throw ToDoNotFoundException when ToDo not found by ID")
    void findById_whenNotPresent() {
        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ToDoNotFoundException.class)
                .hasMessageContaining(String.format("ToDo with id %s not found", id));

        verify(mapper, never()).fromEntityToResponseDto(any());
    }

    @Test
    @DisplayName("should return all ToDos as responseDTOs")
    void findAll() {
        List<ToDoResponseDto> expected = List.of(responseDto, responseDto, responseDto);

        int pageSize = 5, pageNumber = 1;
        List<ToDo> toDos = List.of(todo, todo, todo);

        when(repository.findAll(pageSize, pageNumber))
                .thenReturn(toDos);
        when(mapper.fromEntityToResponseDto(any(ToDo.class)))
                .thenReturn(responseDto);

        List<ToDoResponseDto> actual = service.findAll(pageSize, pageNumber);

        assertThat(actual)
                .isNotEmpty()
                .containsExactlyElementsOf(expected)
                .hasSameSizeAs(expected);
    }

    @Test
    @DisplayName("should save requestToDo and return responseDTO")
    void save() {
        when(mapper.fromRequestToEntity(any(ToDoRequestDto.class)))
                .thenReturn(todo);
        when(repository.save(any(ToDo.class)))
                .thenReturn(todo);
        when(mapper.fromEntityToResponseDto(todo))
                .thenReturn(responseDto);

        ToDoResponseDto actual = service.save(requestDto);

        assertThat(actual).isEqualTo(responseDto);

        verify(mapper, times(1)).fromEntityToResponseDto(any(ToDo.class));
        verify(mapper, times(1)).fromRequestToEntity(any(ToDoRequestDto.class));
    }

    @Test
    @DisplayName("should update an existing ToDo when present")
    void update_whenPresent() {
        when(mapper.fromRequestToEntity(any(ToDoRequestDto.class)))
                .thenReturn(todo);
        when(repository.update(any(ToDo.class), any(UUID.class)))
                .thenReturn(todo);
        when(mapper.fromEntityToResponseDto(todo))
                .thenReturn(responseDto);

        ToDoResponseDto actual = service.update(requestDto, id);

        assertThat(actual).isEqualTo(responseDto);

        verify(mapper, times(1)).fromRequestToEntity(any(ToDoRequestDto.class));
        verify(mapper, times(1)).fromEntityToResponseDto(any(ToDo.class));
    }

    @Test
    @DisplayName("should throw ToDoNotFoundException when updating a non-existent ToDo")
    void update_whenNotPresent() {
        when(mapper.fromRequestToEntity(any(ToDoRequestDto.class)))
                .thenReturn(todo);
        when(repository.update(any(ToDo.class), any(UUID.class)))
                .thenThrow(new ToDoNotFoundException(UUID.fromString(id)));

        assertThatThrownBy(() -> service.update(requestDto, id))
                .isInstanceOf(ToDoNotFoundException.class)
                .hasMessageContaining(String.format("ToDo with id %s not found", id));

        verify(mapper, times(1)).fromRequestToEntity(any(ToDoRequestDto.class));
        verify(mapper, never()).fromEntityToResponseDto(any(ToDo.class));
    }

    @Test
    @DisplayName("should delete ToDo by ID when present")
    void delete_whenPresent() {
        doNothing().when(repository).delete(any(UUID.class));

        String actual = service.delete(id);

        assertThat(actual).isEqualTo(String.format("ToDo with id: %s has been deleted", id));
        verify(repository, times(1)).delete(any(UUID.class));
    }

    @Test
    @DisplayName("should throw ToDoNotFoundException when deleting a non-existent ToDo")
    void delete_whenNotPresent() {
        doThrow(new ToDoNotFoundException(UUID.fromString(id)))
                .when(repository).delete(any(UUID.class));

        assertThatThrownBy(() -> repository.delete(UUID.fromString(id)))
                .isInstanceOf(ToDoNotFoundException.class)
                .hasMessageContaining(String.format("ToDo with id %s not found", id));

        verify(repository, times(1)).delete(any(UUID.class));
    }
}