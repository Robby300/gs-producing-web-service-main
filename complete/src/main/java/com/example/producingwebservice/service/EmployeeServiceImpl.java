package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private final static String ID_NOT_FOUND_MESSAGE = "Id not found";
    //todo не используется // done

    private final EmployeeRepository employeeRepository;


    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    //todo Общие замечание
    // не очень что методы возвращают ResponseEntity. Оборачивай в контроллере
    // done

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
    }
}
