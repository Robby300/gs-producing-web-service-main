package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    Employee save(Employee employee);

    void delete(Employee employee);
}
