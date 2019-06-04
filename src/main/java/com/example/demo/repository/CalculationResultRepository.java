package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CalculationResult;

public interface CalculationResultRepository extends JpaRepository<CalculationResult, Integer> {


}