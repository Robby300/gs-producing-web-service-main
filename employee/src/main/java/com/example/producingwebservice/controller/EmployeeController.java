package com.example.producingwebservice.controller;


import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/employees")
@Tag(name = "Employee controller")
public class EmployeeController {

	private final EmployeeService employeeService;

	@GetMapping()
	@Operation(summary = "Find all employee")
	public List<EmployeeDto> findAll() {
		log.info("Find all employee");
		return employeeService.findAll();
	}

	@PostMapping()
	@Operation(summary = "Save all employee")
	public ResponseEntity<List<EmployeeResponse>> saveAll(
			@RequestBody List<EmployeeDto> employeeDtos) {
		List<EmployeeResponse> employeeResponses = employeeService.saveAll(employeeDtos);
		log.info("save all employee = {}", employeeDtos);
		return new ResponseEntity<>(employeeResponses, HttpStatus.CREATED);
	}

	@GetMapping("/{uuid}")
	@Operation(summary = "Get employee by uuid")
	public EmployeeDto getByUuid(@PathVariable("uuid") String uuid) {
		log.info("Get employee by uuid = {}", uuid);
		return employeeService.findByUuid(uuid);
	}

	@GetMapping("/{uuid}/pdf")
	@Operation(summary = "Get employee PDF by uuid")
	public ResponseEntity<InputStreamResource> getPdfByUuid(@PathVariable("uuid") String uuid) {
		log.info("Get employee PDF by uuid = {}", uuid);
		return employeeService.getEmployeePdfResponseEntity(uuid);
	}

	@PutMapping("/{uuid}")
	@Operation(summary = "Update employee by uuid")
	public EmployeeResponse update(
			@PathVariable("uuid") String uuid, @RequestBody EmployeeDto employeeDto) {
		log.info("Update employee by uuid = {}", uuid);
		return employeeService.update(uuid, employeeDto);
	}

	@DeleteMapping("/{uuid}")
	@Operation(summary = "Delete employee by uuid")
	public void delete(@PathVariable String uuid) {
		log.info("Delete employee by uuid = {}", uuid);
		employeeService.deleteByUuid(uuid);
	}

	@PutMapping("/{uuid}/task/{task_id}")
	@Operation(summary = "Assign task by task_id to employee by uuid")
	public EmployeeResponse assignTask(
			@PathVariable("uuid") String uuid, @PathVariable("task_id") long taskId) {
		log.info("Assign task id = {} to employee by uuid = {}", taskId, uuid);
		return employeeService.assignTaskToEmployee(uuid, taskId);
	}

	@DeleteMapping("/{uuid}/task/{task_id}")
	@Operation(summary = "Unassign task by task_id to employee by uuid")
	public EmployeeResponse unAssignTask(
			@PathVariable("uuid") String uuid, @PathVariable("task_id") long taskId) {
		log.info("Unassigned task id = {} to employee by uuid = {}", taskId, uuid);
		return employeeService.unAssignTaskFromEmployee(uuid, taskId);
	}
}
