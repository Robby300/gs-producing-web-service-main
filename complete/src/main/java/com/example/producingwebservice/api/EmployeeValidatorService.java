package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Employee;

public interface EmployeeValidatorService {
    boolean isValidInput(Employee employee);
    String getViolationsMessage(Employee employee);
}
