package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;

import java.util.List;

public interface ToDoService {
    ToDoResponseDto findById(String id);
    List<ToDoResponseDto> findAll(int pageSize, int pageNumber);
    ToDoResponseDto save(ToDoRequestDto toDoRequestDto);
    ToDoResponseDto update(ToDoRequestDto toDoRequestDto, String id);
    String delete(String id);
}
