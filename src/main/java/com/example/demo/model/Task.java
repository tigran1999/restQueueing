package com.example.demo.model;

import com.example.demo.model.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "task_type")
    @Enumerated(value = EnumType.STRING)
    private TaskType taskType;

    @Column(name = "task_name")
    private String taskName;

}
