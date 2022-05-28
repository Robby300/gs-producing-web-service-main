package com.example.producingwebservice.controller;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping()
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping()
    public void create(@RequestBody List<Employee> employees) {
        log.info("POST request received with parameter = {}", employees);
        employees.forEach(employeeService::save);
    }


    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Employee employee) {
        return employee;
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable("id") Employee employeeFromRepo,
                           @RequestBody Employee employee) {
        BeanUtils.copyProperties(employee, employeeFromRepo, "id");
        return employeeService.save(employeeFromRepo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Employee employee) {
        employeeService.delete(employee);
    }
}
