package com.example.producingwebservice.api;

import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;

public interface EmployeeValidatorService {
    EmployeeResponse validate(EmployeeDto employeeDto);
}
