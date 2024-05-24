package com.march.doitdoit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private LocalDate dueDate;
    private boolean status;

    // 모든 필드를 포함하는 생성자
    public Todo(String item, LocalDate dueDate, boolean status) {
        this.item = item;
        this.dueDate = dueDate;
        this.status = status;
    }

}
