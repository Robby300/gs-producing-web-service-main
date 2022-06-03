package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;

public interface EmployeeValidatorService {
    EmployeeResponse validate(Employee employee);
}
