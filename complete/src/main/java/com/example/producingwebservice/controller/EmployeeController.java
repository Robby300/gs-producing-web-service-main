package com.example.producingwebservice.controller;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.Task;
import com.example.producingwebservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> saveAll(@RequestBody List<Employee> employees) {
        log.info("POST request received with parameter = {}", employees);
        return employeeService.saveAll(employees);
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Employee employee) {
        return employee;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Employee employeeFromRepo,
                           @RequestBody @Valid Employee employee) {
        BeanUtils.copyProperties(employee, employeeFromRepo, "id");
        return employeeService.save(employeeFromRepo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Employee employee) {
        employeeService.delete(employee);
    }

    @PutMapping("/{employee_id}/task/{task_id}")
    public ResponseEntity<?> assignTask(@PathVariable("employee_id") Employee employee,
                               @PathVariable("task_id") Task task) {
        employee.getTasks().add(task);
        return employeeService.save(employee);
    }

    @DeleteMapping("/{employee_id}/task/{task_id}")
    public ResponseEntity<?> unAssignTask(@PathVariable("employee_id") Employee employee,
                                          @PathVariable("task_id") Task task) {
        employee.getTasks().remove(task);
        return employeeService.save(employee);
    }
}
