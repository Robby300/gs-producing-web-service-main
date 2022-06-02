package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.repository.EmployeeRepository;
import com.example.producingwebservice.service.kafkaService.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private final static String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final EmployeeRepository employeeRepository;
    private final ProducerService producerService;


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteByUuid(String uuid) {
        employeeRepository.deleteEmployeeByUuid(uuid);
    }

    @Override
    public Employee save(Employee employee) {
        employee.setUuid(UUID.randomUUID().toString());
        producerService.produce(employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getByUuid(String uuid) {
        return employeeRepository.findEmployeeByUuid(uuid).orElseThrow(() -> new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
    }
}
