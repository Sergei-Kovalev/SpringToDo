package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToDoRepository {
    Optional<ToDo> findById(UUID id);
    List<ToDo> findAll(int pageSize, int pageNumber);
    ToDo save(ToDo todo);
    ToDo update(ToDo todo, UUID id);
    void delete(UUID id);
}
