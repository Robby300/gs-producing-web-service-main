package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private final static String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final static String EMPLOYEE_CREATED = "New employee was created successfully";
    private final static String EMPLOYEE_DELETED = "Employee was deleted Successfully";
    private final static String EMPLOYEE_UPDATED = "Employee was updated successfully";
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidatorService employeeValidatorService;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Validated
    public ResponseEntity<?> save(Employee employee) {
        if (employeeValidatorService.isValidInput(employee)) {
            return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(employeeValidatorService.getViolationsMessage(employee), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public ResponseEntity<?> saveAll(List<Employee> employees) {
        return new ResponseEntity<>(employees.stream().map(this::save).collect(Collectors.toList()), HttpStatus.CREATED);
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() ->new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
    }
}
