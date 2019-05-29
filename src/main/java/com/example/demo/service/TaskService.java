package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.enums.TaskType;
import com.example.demo.repository.TaskRepository;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

@RestController
public class TaskService {

    private final TaskRepository taskRepository;


    @Value("${file.upload.directory}")
    private String uploadPath;

    private Sort getSort() {
        return new Sort(Sort.Direction.ASC, "id");
    }

    private PageRequest getPageRequest() {
        return PageRequest.of(0, Runtime.getRuntime().availableProcessors(), getSort());
    }

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Scheduled(fixedDelay = 10000)
    public void processTasks() {
        if (taskRepository.countByTaskType(TaskType.IN_PROGRESS) > 0) {
            return;
        }
        List<Task> allByTaskType = taskRepository.findAllByTaskType(TaskType.CREATED, getPageRequest()).getContent();
        changeTasksStatuses(allByTaskType);
        if (allByTaskType.size() > 0) {
            progressTasks(allByTaskType);
        }
    }

    private void changeTasksStatuses(List<Task> tasks) {
        for (Task task : tasks) {
            task.setTaskType(TaskType.IN_PROGRESS);
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
                task.setTaskType(TaskType.FINISHED);
                task.setFinishedDate(new Date());
                taskRepository.save(task);
            }
        });
    }

    public Task saveTask(MultipartFile multipartFile) throws IOException {
        String fileUrl = uploadPath + multipartFile.getOriginalFilename();
        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(new File(fileUrl));
        fileOutputStream.write(multipartFile.getBytes());
        Task task = Task.builder()
                .downloaded(false)
                .fileUrl(fileUrl)
                .createdDate(new Date())
                .build();
        taskRepository.save(task);
        return task;
    }

    public void downloadFile(Task task, HttpServletResponse response) throws IOException {
        File file = new File(task.getFileUrl());
        @Cleanup InputStream fileInputStream = new FileInputStream(file);
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setContentType("application/octet-stream");
        response.setContentLength((int) (file.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        IOUtils.copyLarge(fileInputStream, output);
        output.flush();
    }

}
