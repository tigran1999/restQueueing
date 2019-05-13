package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.model.enums.TaskType;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
                .build();
        taskRepository.save(task);
        return ResponseEntity.ok("Created");
    }

    @Scheduled(fixedDelay = 10000)
    public void processThem() {

        if (taskRepository.countByTaskType(TaskType.INPROGRESS) > 0) {
            return;
        }
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        PageRequest of = PageRequest.of(0, Runtime.getRuntime().availableProcessors(), sort);
        List<Task> allByTaskType = taskRepository.findAllByTaskType(TaskType.CREATED, of).getContent();
        changeTaskStatuses(allByTaskType, TaskType.INPROGRESS);
        if (allByTaskType.size() > 0) {
            progressTasks(allByTaskType);
        }
    }

    private void changeTaskStatuses(List<Task> tasks, TaskType taskType) {
        for (Task task : tasks) {
            task.setTaskType(taskType);
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
                taskRepository.save(task);
            }
        });
    }

}
