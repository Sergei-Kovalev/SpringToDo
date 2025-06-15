package com.emobile.springtodo.repository.impl;

import com.emobile.springtodo.entity.ToDo;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.mapper.JDBCToDoMapper;
import com.emobile.springtodo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ToDoRepositoryImpl implements ToDoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JDBCToDoMapper mapper;


    @Override
    public Optional<ToDo> findById(UUID id) {
        String query = "SELECT * FROM todos WHERE id = ?";

        try {
            ToDo toDo = jdbcTemplate.queryForObject(query, mapper, id.toString());
            return Optional.ofNullable(toDo);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ToDo> findAll() {
        String query = "SELECT * FROM todos";

        try {
            return jdbcTemplate.query(query, mapper);
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public ToDo save(ToDo todo) {
        if (todo.getId() == null) {
            todo.setId(UUID.randomUUID());
        }

        String query = "INSERT INTO todos (id, description, expiration_date, is_done) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(query,
                            todo.getId().toString(),
                            todo.getDescription(),
                            todo.getExpirationDate(),
                            todo.isDone());
        return todo;
    }

    @Override
    public ToDo update(ToDo todo, UUID id) {
        String sql = "UPDATE todos SET description = ?, expiration_date = ?, is_done = ? WHERE id = ?";

        int rowsChanged = jdbcTemplate.update(sql,
                                               todo.getDescription(),
                                               todo.getExpirationDate(),
                                               todo.isDone(),
                                               id.toString());

        if (rowsChanged == 0) {
            throw new ToDoNotFoundException(id);
        }

        return todo;    }

    @Override
    public void delete(ToDo todo) {
        String sql = "DELETE FROM todos WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql, todo.getId().toString());

        if (rowsAffected == 0) {
            throw new RuntimeException("ToDo with id " + todo.getId() + " not found for deletion");
        }
    }
}
