package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;

public interface ValidatorService {
    boolean isValidInput(Employee employee);
    String getViolationsMessage(Employee employee);
}
