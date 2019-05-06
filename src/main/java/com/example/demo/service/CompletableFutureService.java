package com.example.demo.service;

import lombok.Synchronized;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class CompletableFutureService {

    @Async
    public CompletableFuture<String> sayHello() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("method was started");
            try {
                Thread.sleep(10000);         // demonstrate long execution
            } catch (InterruptedException ignored) {}
            return "Hello";
        });
    }


}
