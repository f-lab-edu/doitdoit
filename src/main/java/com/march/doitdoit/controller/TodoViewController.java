package com.march.doitdoit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoViewController {

    @GetMapping("/todos")
    public String showTodoPage() {
        return "todo";
    }
}