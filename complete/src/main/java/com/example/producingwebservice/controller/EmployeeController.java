package com.example.producingwebservice.controller;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping()
    public List<EmployeeDto> findAll() {
        log.info("Find all employee");
        return employeeService.findAll();
    }

    @PostMapping()
    public ResponseEntity<List<EmployeeResponse>> saveAll(@RequestBody List<EmployeeDto> employeeDtos) {
        List<EmployeeResponse> employeeResponses = employeeService.saveAll(employeeDtos);
        return new ResponseEntity<>(employeeResponses, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public EmployeeDto getByUuid(@PathVariable("uuid") String uuid) {
        log.info("Get employee by uuid = {}", uuid);
        return employeeService.findByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    public EmployeeResponse update(@PathVariable("uuid") String uuid,
                                   @RequestBody EmployeeDto employeeDto) {
        log.info("Update employee by uuid = {}", uuid);
        return employeeService.update(uuid, employeeDto);
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable String uuid) {
        log.info("Delete employee by uuid = {}", uuid);
        employeeService.deleteByUuid(uuid);
    }

    @PutMapping("/{uuid}/task/{task_id}")
    public EmployeeResponse assignTask(@PathVariable("uuid") String uuid,
                                       @PathVariable("task_id") Task task) {
        log.info("Assign task = {} to employee by uuid = {}", task, uuid);
        EmployeeDto employeeDto = employeeService.findByUuid(uuid);
        employeeDto.getTasks().add(task);
        return employeeService.save(employeeDto);
    }

    @DeleteMapping("/{uuid}/task/{task_id}")
    public EmployeeResponse unAssignTask(@PathVariable("uuid") String uuid,
                                         @PathVariable("task_id") Task task) {
        log.info("Unassigned task id = {} to employee by uuid = {}", task.getId(), uuid);
        EmployeeDto employeeDto = employeeService.findByUuid(uuid);
        employeeDto.getTasks().remove(task);
        return employeeService.save(employeeDto);
    }
}
