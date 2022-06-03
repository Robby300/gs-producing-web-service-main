package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.repository.EmployeeRepository;
import com.example.producingwebservice.service.kafkaservice.ProducerService;
import com.example.producingwebservice.type.ResponseStatus;
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

    private static final String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final EmployeeRepository employeeRepository;
    private final ProducerService producerService;

    private final EmployeeValidatorService employeeValidatorService;


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteByUuid(String uuid) {
        employeeRepository.deleteEmployeeByUuid(uuid);
    }

    @Override
    public EmployeeResponse save(Employee employee) {
        EmployeeResponse employeeResponse = employeeValidatorService.validate(employee);

        if (employeeResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
            generateUuid(employee);
            producerService.produce(employee);
        }
        return employeeResponse;
    }

    @Override
    public Employee getByUuid(String uuid) {
        return employeeRepository.findEmployeeByUuid(uuid).orElseThrow(() -> new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
    }

    private void generateUuid(Employee employee) {
        if (employee.getUuid() == null) {
            employee.setUuid(UUID.randomUUID().toString());
        }
    }
}
