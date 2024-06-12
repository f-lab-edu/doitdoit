package com.march.doitdoit.service;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
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

        // 여기서 ID가 존재한다는 것을 보장
        given(todoRepository.existsById(testTodo.getId())).willReturn(true);

        // 삭제 시 아무 동작 하지 않도록 설정
        willDoNothing().given(todoRepository).deleteById(testTodo.getId());

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

    @Test
    public void testDelete() {
        willDoNothing().given(todoRepository).deleteById(testTodo.getId());

        todoService.delete(testTodo.getId());

        verify(todoRepository, times(1)).deleteById(testTodo.getId());
        given(todoRepository.findById(testTodo.getId())).willReturn(Optional.empty());
        assertThat(todoRepository.findById(testTodo.getId())).isEmpty();
    }
}