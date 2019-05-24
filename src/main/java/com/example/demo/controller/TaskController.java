package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
public class TaskController {

    private final TaskRepository taskRepository;

    @Value("${file.upload.directory}")
    private String uploadPath;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/task")
    public CompletableFuture<ResponseEntity<?>> test(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                                     HttpServletResponse response
    ) throws IOException {
        Task byDownloaded = taskRepository.findByDownloaded(false);
        if (byDownloaded != null) {
            byDownloaded.setDownloaded(true);
            taskRepository.save(byDownloaded);
            downloadFile(byDownloaded, response);
        } else {
            Task task = saveTask(multipartFile);
            try {
                Thread.sleep(10000); // imitate  long execution
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.setFinishedDate(new Date());
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).build());
    }

    private Task saveTask(MultipartFile multipartFile) throws IOException {
        String fileUrl = uploadPath + multipartFile.getOriginalFilename();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileUrl));
        fileOutputStream.write(multipartFile.getBytes());
        Task task = Task.builder()
                .downloaded(false)
                .fileUrl(fileUrl)
                .createdDate(new Date())
                .build();
        taskRepository.save(task);
        return task;
    }

    private void downloadFile(Task task, HttpServletResponse response) throws IOException {
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