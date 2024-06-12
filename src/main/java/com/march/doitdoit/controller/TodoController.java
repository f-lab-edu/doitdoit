package com.march.doitdoit.controller;

import com.march.doitdoit.domain.Todo;
import com.march.doitdoit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
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
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        Optional<Todo> todo = todoService.get(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Optional<Todo> todoOptional = todoService.get(id);

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

    // delete 메소드
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            todoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
