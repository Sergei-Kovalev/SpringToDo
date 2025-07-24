package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.entity.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ToDoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "done", target = "done")
    ToDo fromRequestToEntity(ToDoRequestDto toDoRequestDto);

    @Mapping(source = "done", target = "done")
    ToDoResponseDto fromEntityToResponseDto(ToDo toDo);

    void updateEntityFromRequest(ToDoRequestDto dto, @MappingTarget ToDo entity);
}
