package com.emobile.springtodo.repository.impl;

import com.emobile.springtodo.entity.ToDo;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.repository.ToDoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ToDoRepositoryImpl implements ToDoRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<ToDo> findById(UUID id) {
        ToDo todo = entityManager.find(ToDo.class, id);
        return Optional.ofNullable(todo);
    }

    @Override
    public List<ToDo> findAll(int pageSize, int pageNumber) {
        String jpql = "SELECT t FROM ToDo t ORDER BY t.id";
        return entityManager.createQuery(jpql, ToDo.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    @Override
    public ToDo save(ToDo todo) {
        if (todo.getId() == null) {
            entityManager.persist(todo);
            return todo;
        } else {
            return entityManager.merge(todo);
        }
    }

    @Transactional
    @Override
    public ToDo update(ToDo todo, UUID id) {
        ToDo toDoFromDb = entityManager.find(ToDo.class, id);
        if (toDoFromDb == null) {
            throw new ToDoNotFoundException(id);
        }
        toDoFromDb.setDescription(todo.getDescription());
        toDoFromDb.setExpirationDate(todo.getExpirationDate());
        toDoFromDb.setDone(todo.isDone());
        return entityManager.merge(toDoFromDb);
    }

    @Override
    public void delete(UUID id) {
        ToDo toDo = entityManager.find(ToDo.class, id);
        if (toDo == null) {
            throw new ToDoNotFoundException(id);
        }
        entityManager.remove(toDo);
    }

    @Override
    public int countByDone(boolean done) {
        String jpql = "SELECT COUNT(t) FROM ToDo t WHERE t.done = :done";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("done", done)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }
}
