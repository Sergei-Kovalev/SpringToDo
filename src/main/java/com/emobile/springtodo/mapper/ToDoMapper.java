package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import org.mapstruct.Mapper;

@Mapper
public interface ToDoMapper {
    ToDo fromRequestToEntity(ToDoRequestDto toDoRequestDto);

    ToDoResponseDto fromEntityToResponseDto(ToDo toDo);
}
