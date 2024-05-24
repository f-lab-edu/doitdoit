package com.march.doitdoit.controller;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Optional<Todo> todoOptional = todoService.getTodoById(id);

        if (!todoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Todo todo = todoOptional.get();
        todo.setItem(todoDetails.getItem());
        todo.setDueDate(todoDetails.getDueDate());
        todo.setStatus(todoDetails.isStatus());

        Todo updatedTodo = todoService.saveTodo(todo);
        return ResponseEntity.ok(updatedTodo);
    }

    // 주석처리된 delete 메소드
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
//        if (!todoService.getTodoById(id).isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        todoService.deleteTodoById(id);
//        return ResponseEntity.noContent().build();
//    }
}