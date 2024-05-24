package com.march.doitdoit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // 추가
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
import static org.mockito.Mockito.doNothing;
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
        objectMapper = new ObjectMapper(); // 객체 매퍼 초기화
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록

        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setItem("Test ToDo");
        testTodo.setDueDate(LocalDate.now().plusDays(1));
        testTodo.setStatus(false);

        given(todoService.getAllTodos()).willReturn(List.of(testTodo));
        given(todoService.getTodoById(1L)).willReturn(Optional.of(testTodo));
        given(todoService.saveTodo(any(Todo.class))).willReturn(testTodo);
        doNothing().when(todoService).deleteTodoById(1L);
    }

        @Test
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].item").value(testTodo.getItem()));
    }

        @Test
    public void testGetTodoById() throws Exception {
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }

    @Test
    public void testCreateTodo() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }

        @Test
    public void testUpdateTodo() throws Exception {
        testTodo.setItem("Updated Test ToDo");

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value(testTodo.getItem()));
    }
}



//    @Test
//    public void testDeleteTodo() throws Exception {
//        mockMvc.perform(delete("/api/todos/1"))
//                .andExpect(status().isNoContent());
//    }