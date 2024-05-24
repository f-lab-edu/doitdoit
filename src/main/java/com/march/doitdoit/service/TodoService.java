package com.march.doitdoit.service;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    //@Autowired 생략 가능
    //this.todoRepository로 생성자 이미 있으나 더 명확히 보여주기 위해 표시용
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

//    public void deleteTodoById(Long id) {
//        todoRepository.deleteById(id);
//    }
}