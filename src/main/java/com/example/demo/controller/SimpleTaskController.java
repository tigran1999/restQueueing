package com.example.demo.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CalculationResult;
import com.example.demo.repository.CalculationResultRepository;
import com.example.demo.service.SimpleTaskService;

@RestController
public class SimpleTaskController {

	@Autowired
    private SimpleTaskService taskService;
	
	@Autowired
    private CalculationResultRepository resultRepository;


    @GetMapping("/calculate")
    public int calculate(@RequestParam(name = "id", required = true) Integer id) throws IOException {
        Optional<CalculationResult> optionalResult = resultRepository.findById(id);
        if (optionalResult.isPresent()) {
        	CalculationResult result = optionalResult.get();
            return result.getResult();
        } else {
        	
        	
            return taskService.calculateResultInQueue(id);
        }
    }

}