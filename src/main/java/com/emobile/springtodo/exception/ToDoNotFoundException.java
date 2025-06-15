package com.emobile.springtodo.exception;

import java.util.UUID;

public class ToDoNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "ToDo with id %s not found";

    public ToDoNotFoundException(UUID id) {
        super(String.format(DEFAULT_MESSAGE, id.toString()));
    }
}
