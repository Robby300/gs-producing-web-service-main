package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.kafka.ProducerService;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import com.example.producingwebservice.model.TaskDto;
import com.example.producingwebservice.repository.EmployeeRepository;
import com.example.producingwebservice.type.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.producingwebservice.support.PdfReportGenerator.getEmployeePdfReport;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String EMPLOYEES_REPORT_PDF = "inline; filename=employeesReport.pdf";
	private static final String UUID_NOT_FOUND = "uuid not found";
	private final EmployeeRepository employeeRepository;
	private final TaskService taskService;
	private final ProducerService producerService;
	private final EmployeeValidatorService employeeValidatorService;

	public ResponseEntity<InputStreamResource> getEmployeePdfResponseEntity(String uuid) {
		EmployeeDto foundEmployeeDto = findByUuid(uuid);
		ByteArrayInputStream employeePdf = getEmployeePdfReport(foundEmployeeDto);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(CONTENT_DISPOSITION, EMPLOYEES_REPORT_PDF);

		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(employeePdf));
	}

	@Override
	public List<EmployeeDto> findAll() {
		return employeeRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteByUuid(String uuid) {
		employeeRepository.deleteEmployeeByUuid(uuid);
	}

	@Override
	public EmployeeResponse save(EmployeeDto employeeDto) {
		EmployeeResponse employeeResponse = employeeValidatorService.validate(employeeDto);
		if (employeeResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
			generateUuid(employeeDto);
			producerService.produce(employeeDto);
		}
		return employeeResponse;
	}

	@Override
	public EmployeeDto findByUuid(String uuid) {
		Employee employee = employeeRepository
				.findEmployeeByUuid(uuid)
				.orElseThrow(() -> new EmployeeNotFoundException(UUID_NOT_FOUND));
		return toDto(employee);
	}

	@Override
	public List<EmployeeResponse> saveAll(List<EmployeeDto> employeeDtos) {
		return employeeDtos.stream().map(this::save).collect(Collectors.toList());
	}

	@Override
	public EmployeeResponse update(String uuid, EmployeeDto employeeDto) {
		EmployeeDto employeeDtoFromRepo = findByUuid(uuid);
		employeeDtoFromRepo.setName(employeeDto.getName());
		employeeDtoFromRepo.setSalary(employeeDto.getSalary());
		employeeDtoFromRepo.setPosition(employeeDto.getPosition());
		employeeDtoFromRepo.setTasks(employeeDto.getTasks());
		log.info("Update employee = {}", employeeDtoFromRepo);
		return save(employeeDtoFromRepo);
	}

	@Override
	public EmployeeResponse assignTaskToEmployee(String uuid, long taskId) {
		EmployeeDto employeeDto = findByUuid(uuid);
		TaskDto taskDto = taskService.findById(taskId);
		employeeDto.getTasks().add(toEntity(taskDto));
		return save(employeeDto);
	}

	@Override
	public EmployeeResponse unAssignTaskFromEmployee(String uuid, long taskId) {
		EmployeeDto employeeDto = findByUuid(uuid);
		TaskDto taskDto = taskService.findById(taskId);
		employeeDto.getTasks().remove(toEntity(taskDto));
		return save(employeeDto);
	}

	private void generateUuid(EmployeeDto employeeDto) {
		if (employeeDto.getUuid() == null) {
			employeeDto.setUuid(UUID.randomUUID().toString());
		}
	}

	private EmployeeDto toDto(Employee employee) {
		return new EmployeeDto(
				employee.getId(),
				employee.getUuid(),
				employee.getName(),
				employee.getSalary(),
				employee.getPosition(),
				employee.getTasks());
	}

	private Task toEntity(TaskDto dto) {
		return new Task(dto.getId(), dto.getDescription());
	}
}
