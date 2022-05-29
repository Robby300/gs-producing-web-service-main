package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import java.util.List;
import java.util.Set;
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
    private final MessageService messageService;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Validated
    public ResponseEntity<?> save(Employee employee) {
        validateInput(employee);
        return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public ResponseEntity<?> saveAll(List<Employee> employees) {
        List<? extends ResponseEntity<?>> collectResponses = employees.stream().map(this::save).collect(Collectors.toList());
        return new ResponseEntity<>(collectResponses, HttpStatus.CREATED);
    }

    void validateInput(Employee employee) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
