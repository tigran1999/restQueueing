package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Task findByDownloadedAndUser(boolean downloaded, User user);

}
