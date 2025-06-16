package com.emobile.springtodo.swagger;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "ToDo API", description = "API для управления списком дел")
public interface IToDoController {

    @Operation(
            summary = "Получение задачи",

            description = "Получение задачи по id",
            responses = {
                    @ApiResponse(
                            description = "Задача найдена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "id": "989b3265-ccc7-4480-bb9a-cf1c7026ed39",
                                                                "description": "bla bla bla",
                                                                "expirationDate": "2025-07-18T00:00:00.546",
                                                                "done": false
                                                            }
                                                            """
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Задача не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2023-10-31T18:09:02.598535700Z"
                                                                "status":404,
                                                                "error":"ToDo with id 859b3265-ccc7-4480-bb9a-cf1c7026ed39 not found",
                                                                "path":"/todo/859b3265-ccc7-4480-bb9a-cf1c7026ed39"
                                                            }
                                                            """
                                            )
                                    }
                            ))
            }
    )
    ToDoResponseDto findById(@UUID @PathVariable String id);

    @Operation(
            summary = "Получение всего списка дел",
            description = "Получение всего списка дел с пагинацией",
            responses = {
                    @ApiResponse(
                            description = "Список дел получен",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            [
                                                                {
                                                                    "id":"989b3265-ccc7-4480-bb9a-cf1c7026ed39",
                                                                    "description":"bla bla bla",
                                                                    "expirationDate":"2025-07-18T00:00:00.546",
                                                                    "done":false
                                                                },
                                                                {
                                                                    "id":"cd3d29fe-6991-4cb3-992d-cef17e91decb",
                                                                    "description":"bla2 bla bla",
                                                                    "expirationDate":"2025-07-15T00:00:00",
                                                                    "done":true
                                                                }
                                                            ]"""
                                            )
                                    }
                            ))
            }
    )
    List<ToDoResponseDto> findAll(@RequestParam @Positive int pageSize, @RequestParam @Positive int pageNumber);

    @Operation(
            summary = "Сохранение новой задачи",
            description = "Сохраняет задачу",
            responses = {
                    @ApiResponse(
                            description = "Задача сохранена",
                            responseCode = "200",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "id":"cd3d29fe-6991-4cb3-992d-cef17e91decb",
                                                                "description":"Hello World",
                                                                "expirationDate":"2025-11-25T12:59:10",
                                                                "done":false
                                                            }"""
                                            )
                                    }
                            ))
            }
    )
    ToDoResponseDto save(@RequestBody @Validated ToDoRequestDto requestDto);

    @Operation(
            summary = "Обновление задачи",
            description = "Обновление задачи по id",
            responses = {
                    @ApiResponse(
                            description = "Задача обновлена",
                            responseCode = "200",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "id":"cd3d29fe-6991-4cb3-992d-cef17e91decb",
                                                                "description":"Hello World",
                                                                "expirationDate":"2025-11-25T12:59:10",
                                                                "done":false
                                                            }"""
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Задача не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2023-10-31T18:09:02.598535700Z"
                                                                "status":404,
                                                                "error":"ToDo with id 859b3265-ccc7-4480-bb9a-cf1c7026ed39 not found",
                                                                "path":"/todo/859b3265-ccc7-4480-bb9a-cf1c7026ed39"
                                                            }
                                                            """
                                            )
                                    }
                            ))
            }
    )
    ToDoResponseDto update(@RequestBody @Validated ToDoRequestDto requestDto, @PathVariable @UUID String id);

    @Operation(
            summary = "Удаление задачи",
            description = "Удаление задачи по id",
            responses = {
                    @ApiResponse(
                            description = "Задача удалена",
                            responseCode = "200",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    value = "ToDo with id: 859b3265-ccc7-4480-bb9a-cf1c7026ed39 has been deleted"
                                            )
                                    }

                            )),
                    @ApiResponse(
                            description = "Задача не найдена",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "timestamp": "2023-10-31T18:09:02.598535700Z"
                                                                "status":404,
                                                                "error":"ToDo with id 859b3265-ccc7-4480-bb9a-cf1c7026ed39 not found",
                                                                "path":"/todo/859b3265-ccc7-4480-bb9a-cf1c7026ed39"
                                                            }
                                                            """
                                            )
                                    }
                            ))
            }
    )
    String delete(@PathVariable @UUID String id);
}
