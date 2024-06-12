package com.march.doitdoit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private ObjectMapper objectMapper;
    private Todo testTodo;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setItem("Test ToDo");
        testTodo.setDueDate(LocalDate.now().plusDays(1));
        testTodo.setStatus(false);

        given(todoService.getAllTodos()).willReturn(List.of(testTodo));
        given(todoService.get(1L)).willReturn(Optional.of(testTodo));
        given(todoService.saveTodo(any(Todo.class))).willReturn(testTodo);
        Mockito.doThrow(new RuntimeException()).when(todoService).delete(1L);
    }

    @Test
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item").value(testTodo.getItem()));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }

    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }

    @Test
    public void testUpdate() throws Exception {
        testTodo.setItem("Updated Test ToDo");

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }

    @Test
    public void testDelete() throws Exception {
        willDoNothing().given(todoService).delete(1L);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        given(todoService.get(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isNotFound());
    }

}
