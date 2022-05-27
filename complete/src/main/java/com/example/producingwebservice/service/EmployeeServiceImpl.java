package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.exception.NotValidException;
import com.example.producingwebservice.repository.EmployeeRepository;
import https.www_rob_com.gen.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    private final static String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final static String EMPLOYEE_CREATED = "New employee was created successfully";
    private final static String EMPLOYEE_DELETED = "Employee was deleted Successfully";
    private final static String EMPLOYEE_UPDATED = "Employee was updated successfully";
    private final EmployeeRepository employeeRepository;
    private final MessageService messageService;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }
}
