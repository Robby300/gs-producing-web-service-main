package com.example.producingwebservice.controller;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping()
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping()
    public Employee create(@RequestBody Employee employee) {
        return employeeService.save(employee);
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
