package com.emobile.springtodo.service.impl;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.ToDoMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import com.emobile.springtodo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;

    @Override
    @Cacheable(value = "todos", key = "#id")
    public ToDoResponseDto findById(String id) {
        return toDoRepository.findById(UUID.fromString(id))
                .map(toDoMapper::fromEntityToResponseDto)
                .orElseThrow(() -> new ToDoNotFoundException(UUID.fromString(id)));
    }

    @Override
    @Cacheable(value = "todosList", key = "'pageSize:' + #pageSize + ':pageNumber:' + #pageNumber")
    public List<ToDoResponseDto> findAll(int pageSize, int pageNumber) {
        return toDoRepository.findAll(pageSize, pageNumber)
                .stream()
                .map(toDoMapper::fromEntityToResponseDto)
                .toList();
    }

    @Override
    @CachePut(value = "todos", key = "#result.id")
    @CacheEvict(value = "todosList", allEntries = true)
    public ToDoResponseDto save(ToDoRequestDto toDoRequestDto) {
        ToDo forSave = toDoMapper.fromRequestToEntity(toDoRequestDto);
        ToDo saved = toDoRepository.save(forSave);
        return toDoMapper.fromEntityToResponseDto(saved);
    }

    @Override
    @CachePut(value = "todos", key = "#result.id")
    @CacheEvict(value = "todosList", allEntries = true)
    public ToDoResponseDto update(ToDoRequestDto toDoRequestDto, String id) {
        ToDo forUpdate = toDoMapper.fromRequestToEntity(toDoRequestDto);
        ToDo updated = toDoRepository.update(forUpdate, UUID.fromString(id));
        return toDoMapper.fromEntityToResponseDto(updated);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "todos", key = "#id"),
            @CacheEvict(value = "todosList", allEntries = true)
    })
    public String delete(String id) {
        toDoRepository.delete(UUID.fromString(id));
        return String.format("ToDo with id: %s has been deleted", id);
    }
}
