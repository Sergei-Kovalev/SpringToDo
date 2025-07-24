package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.ToDo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ToDoRepository extends JpaRepository<ToDo, UUID> {
    List<ToDo> findAllByOrderById(Pageable pageable);
    int countByDone(boolean done);
}
