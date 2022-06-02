package com.example.producingwebservice.controller;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/employees")
public class EmployeeController {


    private final EmployeeService employeeService;
    private final EmployeeValidatorService employeeValidatorService;

    @GetMapping()
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping()
    public ResponseEntity<?> saveAll(@RequestBody List<Employee> employees) {
        List<ResponseEntity<?>> responseEntities = employees
                .stream()
                .map(this::save)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseEntities, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public Employee getByUuid(@PathVariable("uuid") String uuid) {
        log.info("Get employee by uuid = {}", uuid);
        return employeeService.getByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(@PathVariable("uuid") String uuid,
                                    @RequestBody Employee employee) {
        log.info("Update employee by id = {}", employee.getId());
        Employee employeeFromRepo = employeeService.getByUuid(uuid);
        BeanUtils.copyProperties(employee, employeeFromRepo, "id", "uuid");
        return save(employeeFromRepo);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable String uuid) {
        log.info("Delete employee by uuid = {}", uuid);
        employeeService.deleteByUuid(uuid);
    }

    @PutMapping("/{uuid}/task/{task_id}")
    public Employee assignTask(@PathVariable("uuid") String uuid,
                               @PathVariable("task_id") Task task) {
        log.info("Assign task id = {} to employee by uuid = {}", task.getId(), uuid);
        Employee employee = employeeService.getByUuid(uuid);
        employee.getTasks().add(task);
        return employeeService.save(employee);
    }

    @DeleteMapping("/{uuid}/task/{task_id}")
    public Employee unAssignTask(@PathVariable("uuid") String uuid,
                                 @PathVariable("task_id") Task task) {
        log.info("Unassigned task id = {} to employee by uuid = {}", task.getId(), uuid);
        Employee employee = employeeService.getByUuid(uuid);
        employee.getTasks().remove(task);
        return employeeService.save(employee);
    }

    private ResponseEntity<?> save(Employee employee) {
        if (employeeValidatorService.isValidInput(employee)) {
            return new ResponseEntity<>(employeeService.save(employee), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(employeeValidatorService.getViolationsMessage(employee), HttpStatus.FORBIDDEN);
        }
    }
}
