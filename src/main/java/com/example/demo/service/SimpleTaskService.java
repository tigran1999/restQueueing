package com.example.demo.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CalculationResult;
import com.example.demo.repository.CalculationResultRepository;

@Service
public class SimpleTaskService {

	@Autowired
    private CalculationResultRepository resultRepository;
	
	@Autowired
    private ExecutorService executorService;
	
    public Integer calculateResultInQueue(int i) {

    	Future<Integer> result = executorService.submit(() -> {
    		return calculateResult(i);        	
        });
    	
    	try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
    }

    public Integer calculateResult(int id) {
    	int result = -1;
        Optional<CalculationResult> optionalResult = resultRepository.findById(id);
        if (optionalResult.isPresent()) {
            result = optionalResult.get().getResult();
        } else {
	        try {
	            Thread.sleep(10000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        result = id*id;
        }
        return result;
    	
    }
}
