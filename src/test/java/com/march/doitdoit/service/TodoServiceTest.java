package com.march.doitdoit.service;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.repository.TodoRepository;
import com.march.doitdoit.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;

    private Todo testTodo;

    @BeforeEach
    public void setUp() {
        testTodo = new Todo();
        testTodo.setId(1L);  // ID를 설정하여 findById가 정확히 매칭되도록 합니다.
        testTodo.setItem("Test ToDo");
        testTodo.setDueDate(LocalDate.now().plusDays(1));
        testTodo.setStatus(false);

        given(todoRepository.save(any(Todo.class))).willReturn(testTodo);
        given(todoRepository.findAll()).willReturn(List.of(testTodo));
        given(todoRepository.findById(testTodo.getId())).willReturn(Optional.of(testTodo));
    }

    @Test
    public void testSaveTodo() {
        Todo savedTodo = todoService.saveTodo(testTodo);
        assertThat(savedTodo.getItem()).isEqualTo(testTodo.getItem());
    }

    @Test
    public void testGetAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).getItem()).isEqualTo(testTodo.getItem());
    }

//    @Test
//    public void testDeleteTodo() {
//        // Mocking the void method.
//        doNothing().when(todoRepository).deleteById(testTodo.getId());
//        todoService.deleteTodoById(testTodo.getId());
//
//        verify(todoRepository, times(1)).deleteById(testTodo.getId());
//        given(todoRepository.findById(testTodo.getId())).willReturn(Optional.empty());
//        assertThat(todoRepository.findById(testTodo.getId())).isEmpty();
//    }
}