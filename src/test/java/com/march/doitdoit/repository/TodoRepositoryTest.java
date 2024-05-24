package com.march.doitdoit.repository;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testCreateReadDeleteTodo() {
        Todo testTodo = new Todo("Test ToDo", LocalDate.now().plusDays(1), false);
        Todo savedTodo = todoRepository.save(testTodo);

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1).contains(savedTodo);

        todoRepository.delete(savedTodo);
        todos = todoRepository.findAll();
        assertThat(todos).isEmpty();
    }
}