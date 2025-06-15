package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToDoRepository {
    Optional<ToDo> findById(UUID id);
    List<ToDo> findAll(int limit, int offset);
    ToDo save(ToDo todo);
    ToDo update(ToDo todo, UUID id);
    void delete(ToDo todo);
}
