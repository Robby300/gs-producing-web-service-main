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

import javax.validation.Valid;
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
        //todo переносы в стриме + вынести в отдельную переменную. ЗАмечание при данной реализации
        List<ResponseEntity<?>> responseEntities = employees
                .stream()
                .map(this::save)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseEntities, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Long id) {
        log.info("Get employee by id = {}", id);
        return employeeService.getById(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable("id") Employee employeeFromRepo,
                                    @RequestBody @Valid Employee employee) {
        log.info("Update employee by id = {}", employee.getId());
        BeanUtils.copyProperties(employee, employeeFromRepo, "id"); //todo это для чего ? // done копирование игнорируя id
        return employeeService.save(employeeFromRepo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Employee employee) {
        log.info("Delete employee by id = {}", employee.getId());
        employeeService.delete(employee);
    }

    @PutMapping("/{employee_id}/task/{task_id}")
    public Employee assignTask(@PathVariable("employee_id") Employee employee,
                                        @PathVariable("task_id") Task task) {
        log.info("Assign task id = {} to employee by id = {}", task.getId(), employee.getId());
        employee.getTasks().add(task);
        return employeeService.save(employee);
    }

    @DeleteMapping("/{employee_id}/task/{task_id}")
    public Employee unAssignTask(@PathVariable("employee_id") Employee employee,
                                          @PathVariable("task_id") Task task) {
        log.info("Unassigned task id = {} to employee by id = {}", task.getId(), employee.getId());
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
