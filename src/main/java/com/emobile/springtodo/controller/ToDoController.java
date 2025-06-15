package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.service.ToDoService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;

    @GetMapping("/{id}")
    public ToDoResponseDto findById(@UUID @PathVariable String id) {
        return toDoService.findById(id);
    }

    @GetMapping("/all")
    public List<ToDoResponseDto> findAll(@RequestParam @Positive int pageSize,
                                         @RequestParam @Positive int pageNumber) {
        return toDoService.findAll(pageSize, pageNumber);
    }

    @PostMapping
    public ToDoResponseDto save(@RequestBody @Validated ToDoRequestDto requestDto) {
        return toDoService.save(requestDto);
    }

    @PutMapping("/{id}")
    public ToDoResponseDto update(@RequestBody @Validated ToDoRequestDto requestDto,
                                  @PathVariable @UUID String id) {
        return toDoService.update(requestDto, id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable @UUID String id) {
        return toDoService.delete(id);
    }
}
