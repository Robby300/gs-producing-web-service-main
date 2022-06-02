package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    void deleteByUuid(String uuid);

    @Validated
    Employee save(@Valid Employee employee);

    Employee getByUuid(String uuid);
}
