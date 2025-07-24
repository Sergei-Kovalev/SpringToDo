package com.emobile.springtodo.integration;

import com.emobile.springtodo.dto.request.ToDoRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Transactional
@Sql({"/todo-data-init.sql"})
class ToDoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Container
    @ServiceConnection
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:8.0.2")
            .withExposedPorts(6379);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");


    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            connection.execute("FLUSHDB");
        }
    }

    @Test
    @DisplayName("should return response when found by id")
    void getToDo() throws Exception {
        String id = "989b3265-ccc7-4480-bb9a-cf1c7026ed39";
        String expectedJson = """
                {
                    "id": "989b3265-ccc7-4480-bb9a-cf1c7026ed39",
                    "description": "bla bla bla",
                    "expirationDate": '2025-07-18T00:00:00.546',
                    "done": false
                }""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/" + id))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should return error when not found by id")
    void getToDo_notFound() throws Exception {
        String id = "859b3265-ccc7-4480-bb9a-cf1c7026ed39";
        String expectedJson = """
                {
                    "status":404,
                    "error":"ToDo with id 859b3265-ccc7-4480-bb9a-cf1c7026ed39 not found",
                    "path":"/todo/859b3265-ccc7-4480-bb9a-cf1c7026ed39"
                }""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/" + id))
                .andExpect(status().isNotFound())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should return list of all founded todos")
    void findAll() throws Exception {
        String expectedJson = """
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
                ]""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo/all")
                                                   .param("pageSize", "5")
                                                   .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should save ToDo and return it")
    void save() throws Exception {
        ToDoRequestDto requestDto = ToDoRequestDto.builder()
                .description("Hello World")
                .expirationDate(LocalDateTime.of(2025, Month.NOVEMBER, 25, 12, 59, 10))
                .done(false)
                .build();
        String requestStr = objectMapper.writeValueAsString(requestDto);

        String expectedJson = """
                {
                    "description":"Hello World",
                    "expirationDate":"2025-11-25T12:59:10",
                    "done":false
                }""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/todo")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(requestStr))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should update todo by id when it present in db")
    void update_whenPresent() throws Exception {
        String id = "cd3d29fe-6991-4cb3-992d-cef17e91decb";

        ToDoRequestDto requestDto = ToDoRequestDto.builder()
                .description("Hello World")
                .expirationDate(LocalDateTime.of(2025, Month.NOVEMBER, 25, 12, 59, 10))
                .done(false)
                .build();

        String requestStr = objectMapper.writeValueAsString(requestDto);

        String expectedJson = """
                {
                    "id":"cd3d29fe-6991-4cb3-992d-cef17e91decb",
                    "description":"Hello World",
                    "expirationDate":"2025-11-25T12:59:10",
                    "done":false
                }""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/todo/{id}", id)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(requestStr))
                .andExpect(status().isOk())
                .andReturn();

        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should return error of updating todo when id not present in db")
    void update_whenNotPresent() throws Exception {
        String id = "859b3265-ccc7-4480-bb9a-cf1c7026ed39";

        ToDoRequestDto requestDto = ToDoRequestDto.builder()
                .description("Hello World")
                .expirationDate(LocalDateTime.of(2025, Month.NOVEMBER, 25, 12, 59, 10))
                .done(false)
                .build();

        String requestStr = objectMapper.writeValueAsString(requestDto);

        String expectedJson = """
                {
                    "status":404,
                    "error":"ToDo with id 859b3265-ccc7-4480-bb9a-cf1c7026ed39 not found",
                    "path":"/todo/859b3265-ccc7-4480-bb9a-cf1c7026ed39"
                }""";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/todo/{id}", id)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(requestStr))
                .andExpect(status().isNotFound())
                .andReturn();
        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("should delete todo by id when it present in db")
    void delete_whenPresent() throws Exception {
        String id = "cd3d29fe-6991-4cb3-992d-cef17e91decb";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/todo/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        String expectedMessage = String.format("ToDo with id: %s has been deleted", id);

        assertThat(response).isEqualTo(expectedMessage);
    }
}
