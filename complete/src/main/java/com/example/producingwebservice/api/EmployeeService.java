package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    @Validated
    Employee save(@Valid Employee employee);

    void delete(Employee employee);

    Employee getById(Long id);
}
