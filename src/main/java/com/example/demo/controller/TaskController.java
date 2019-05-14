package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.enums.TaskType;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;

@RestController
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/task/{name}")
    public ResponseEntity<?> addTask(@PathVariable String name) {
        Task task = Task.builder()
                .taskName(name)
                .taskType(TaskType.CREATED)
                .createdDate(new Date())
                .build();
        taskRepository.save(task);
        return ResponseEntity.ok("Created by  "+task.getId() + " id");
    }

    @GetMapping("/checkStatus/{id}")
    public ResponseEntity<?> checkStatus(@PathVariable UUID id){
        Optional<Task> byId = taskRepository.getById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok("Your task status is " + byId.get().getTaskType());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with " + id + " doesn't exist");
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void processThem() {

        if (taskRepository.countByTaskType(TaskType.INPROGRESS) > 0) {
            return;
        }
        Sort sort = new Sort(Sort.Direction.ASC, "createdDate");
        PageRequest of = PageRequest.of(0, Runtime.getRuntime().availableProcessors(), sort);
        List<Task> allByTaskType = taskRepository.findAllByTaskType(TaskType.CREATED, of).getContent();
        changeTaskStatuses(allByTaskType);
        if (allByTaskType.size() > 0) {
            progressTasks(allByTaskType);
        }
    }

    private void changeTaskStatuses(List<Task> tasks) {
        for (Task task : tasks) {
            task.setTaskType(TaskType.INPROGRESS);
            taskRepository.save(task);
        }
    }

    private void progressTasks(List<Task> tasks) {
        Executors.newCachedThreadPool().submit(() -> {
            for (Task task : tasks) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.setTaskName(task.getTaskName() + "_progress");
                task.setTaskType(TaskType.FINISHED);
                task.setFinishedDate(new Date());
                taskRepository.save(task);
            }
        });
    }

}
