package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.enums.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Page<Task> findAllByTaskType(TaskType taskType, Pageable pageable);

    int countByTaskType(TaskType taskType);

}