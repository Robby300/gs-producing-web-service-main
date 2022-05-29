package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    @Validated
    ResponseEntity<?> save(@Valid Employee employee);

    void delete(Employee employee);

    ResponseEntity<?> saveAll(List<Employee> employees);
}
