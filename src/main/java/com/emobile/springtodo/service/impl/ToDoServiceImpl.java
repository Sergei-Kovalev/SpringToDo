package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import com.emobile.springtodo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;


    @Override
    public ToDoResponseDto findById(UUID id) {
        return toDoRepository.findById(id)
                .map(toDoMapper::fromEntityToResponseDto)
                .orElseThrow(() -> new ToDoNotFoundException(id));
    }

    @Override
    public List<ToDoResponseDto> findAll(int limit, int offset) {
        return toDoRepository.findAll(limit, offset)
                .stream()
                .map(toDoMapper::fromEntityToResponseDto)
                .toList();
    }

    @Override
    public ToDoResponseDto save(ToDoRequestDto toDoRequestDto) {
        ToDo forSave = toDoMapper.fromRequestToEntity(toDoRequestDto);
        ToDo saved = toDoRepository.save(forSave);
        return toDoMapper.fromEntityToResponseDto(saved);
    }

    @Override
    public ToDoResponseDto update(ToDoRequestDto toDoRequestDto, UUID id) {
        ToDo forUpdate = toDoMapper.fromRequestToEntity(toDoRequestDto);
        ToDo updated = toDoRepository.update(forUpdate, id);
        return toDoMapper.fromEntityToResponseDto(updated);
    }

    @Override
    public String delete(UUID id) {
        toDoRepository.delete(id);
        return String.format("ToDo with id: %s was deleted", id);
    }
}
