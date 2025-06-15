package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.emobile.springtodo.dto.response.ToDoResponseDto;
import com.emobile.springtodo.exception.ToDoNotFoundException;
import com.emobile.springtodo.exception.handler.ControllersExceptionHandler;
import com.emobile.springtodo.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ToDoControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ToDoService service;
    @InjectMocks
    private ToDoController controller;

    private String id;
    private ToDoRequestDto requestDto;
    private ToDoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        id = "1fcc470d-a691-4d0d-9a23-b8b455c5f586";
        String description = "bla bla bla";
        LocalDateTime expirationDate = LocalDateTime.now().plusYears(1);
        boolean done = false;

        requestDto = ToDoRequestDto.builder()
                .description(description)
                .expirationDate(expirationDate)
                .done(done)
                .build();

        responseDto = ToDoResponseDto.builder()
                .id(UUID.fromString(id))
                .description(description)
                .expirationDate(expirationDate)
                .done(done)
                .build();
    }

    @Test
    @DisplayName("should return response when found by id")
    void findById_whenPresent() throws Exception {
        String jsonResponseQuery = objectMapper.writeValueAsString(responseDto);

        when(service.findById(id)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonResponseQuery))
                .andDo(print());

        verify(service).findById(id);
    }

    @Test
    @DisplayName("should return error when not found by id")
    void findById_whenNotPresent() throws Exception {
        when(service.findById(id)).thenThrow(new ToDoNotFoundException(UUID.fromString(id)));

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(String.format("ToDo with id %s not found", id)))
                .andDo(print());
    }

    @Test
    @DisplayName("should return list of all founded todos")
    void findAll() throws Exception {
        int pageSize = 5;
        int pageNumber = 1;
        List<ToDoResponseDto> responseDtoList = List.of(responseDto, responseDto);

        when(service.findAll(pageSize, pageNumber)).thenReturn(responseDtoList);

        String expectedJson = objectMapper.writeValueAsString(responseDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/all")
                                .param("pageSize", String.valueOf(pageSize))
                                .param("pageNumber", String.valueOf(pageNumber))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson))
                .andDo(print());
    }

    @Test
    @DisplayName("should save ToDo and return it")
    void save() throws Exception {
        when(service.save(any(ToDoRequestDto.class))).thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(requestDto);
        String responseJson = objectMapper.writeValueAsString(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andDo(print());
    }

    @Test
    @DisplayName("should update todo by id when it present in db")
    void update_whenPresent() throws Exception {
        when(service.update(any(ToDoRequestDto.class), eq(id))).thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(requestDto);
        String responseJson = objectMapper.writeValueAsString(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("should return error of updating todo when id not present in db")
    void update_whenNotPresent() throws Exception {
        when(service.update(any(ToDoRequestDto.class), eq(id)))
                .thenThrow(new ToDoNotFoundException(UUID.fromString(id)));

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(String.format("ToDo with id %s not found", id)))
                .andDo(print());
    }

    @Test
    @DisplayName("should delete todo by id when it present in db")
    void delete_whenPresent() throws Exception {
        String expectedMessage = String.format("ToDo with id: %s has been deleted", id);

        when(service.delete(id)).thenReturn(expectedMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage))
                .andDo(print());

        verify(service).delete(id);
    }

    @Test
    @DisplayName("should return error of deleting todo when id not present in db")
    void delete_whenNotPresent() throws Exception {
        when(service.delete(id)).thenThrow(new ToDoNotFoundException(UUID.fromString(id)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/todo/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(String.format("ToDo with id %s not found", id)))
                .andDo(print());

        verify(service).delete(id);
    }
}