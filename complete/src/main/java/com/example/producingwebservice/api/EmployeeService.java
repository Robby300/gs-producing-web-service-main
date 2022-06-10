package com.example.producingwebservice.api;

import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAll();

    void deleteByUuid(String uuid);

    EmployeeResponse save(EmployeeDto employeeDto);

    EmployeeDto findByUuid(String uuid);

    List<EmployeeResponse> saveAll(List<EmployeeDto> employeeDtos);

    EmployeeResponse update(String uuid, EmployeeDto employeeDto);

    EmployeeResponse assignTaskToEmployee(String uuid, long taskId);

    EmployeeResponse unAssignTaskFromEmployee(String uuid, long taskId);

    ResponseEntity<InputStreamResource> getEmployeePdfResponseEntity(String uuid);
}
