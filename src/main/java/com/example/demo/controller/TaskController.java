package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @PostMapping("/task")
    public ResponseEntity<?> test(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                  @RequestParam(name = "taskId", required = false) Integer taskId,
                                  HttpServletResponse response) throws IOException {
        if (taskId == null) {
            taskId = 0;
        }
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent() && !optionalTask.get().isDownloaded()) {
            Task task = optionalTask.get();
            task.setDownloaded(true);
            taskRepository.save(task);
            taskService.downloadFile(task, response);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            Task task = taskService.saveTask(multipartFile);
            return (ResponseEntity.ok(task.getId()));
        }
    }

}