package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    void deleteByUuid(String uuid);

    EmployeeResponse save(Employee employee);

    Employee findByUuid(String uuid);
}
