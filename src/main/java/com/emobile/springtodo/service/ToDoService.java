package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;

import java.util.List;
import java.util.UUID;

public interface ToDoService {
    ToDoResponseDto findById(UUID id);
    List<ToDoResponseDto> findAll(int limit, int offset);
    ToDoResponseDto save(ToDoRequestDto toDoRequestDto);
    ToDoResponseDto update(ToDoRequestDto toDoRequestDto, UUID id);
    String delete(UUID id);
}
