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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.producingwebservice.support.PdfReportGenerator.getEmployeePdfReport;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String EMPLOYEES_REPORT_PDF = "inline; filename=employeesReport.pdf";
	public static final long CACHE_TTL_MINUTES = 30;
	private static final String UUID_NOT_FOUND = "uuid not found";
	private static final String EMPLOYEE_CACHE_PREFIX = "employee:";
	private static final String EMPLOYEE_ALL_CACHE = "employees:all";
	private static final String PDF_CACHE_PREFIX = "employee:pdf:";
	private final EmployeeRepository employeeRepository;
	private final TaskService taskService;
	private final ProducerService producerService;
	private final EmployeeValidatorService employeeValidatorService;
	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	public ResponseEntity<InputStreamResource> getEmployeePdfResponseEntity(String uuid) {
		byte[] cachedPdf = getCachedPdf(uuid);
		if (cachedPdf != null) {
			return buildPdfResponse(new ByteArrayInputStream(cachedPdf));
		}

		EmployeeDto foundEmployeeDto = findByUuid(uuid);
		ByteArrayInputStream reportStream = getEmployeePdfReport(foundEmployeeDto);
		byte[] pdfBytes = reportStream.readAllBytes();
		cachePdf(uuid, pdfBytes);
		return buildPdfResponse(new ByteArrayInputStream(pdfBytes));
	}

	private ResponseEntity<InputStreamResource> buildPdfResponse(ByteArrayInputStream pdfStream) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(CONTENT_DISPOSITION, EMPLOYEES_REPORT_PDF);
		return ResponseEntity.ok()
				.headers(httpHeaders)
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfStream));
	}

	@Override
	public List<EmployeeDto> findAll() {
		String cached = stringRedisTemplate.opsForValue().get(EMPLOYEE_ALL_CACHE);
		if (cached != null) {
			try {
				return objectMapper.readValue(cached, new TypeReference<List<EmployeeDto>>() {});
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cached employee list", e);
			}
		}

		List<EmployeeDto> result = employeeRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());

		try {
			stringRedisTemplate.opsForValue().set(
					EMPLOYEE_ALL_CACHE, objectMapper.writeValueAsString(result),
					CACHE_TTL_MINUTES, TimeUnit.MINUTES);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize employee list for cache", e);
		}
		return result;
	}

	@Override
	public void deleteByUuid(String uuid) {
		employeeRepository.deleteEmployeeByUuid(uuid);
		evictEmployeeCache(uuid);
	}

	@Override
	public EmployeeResponse save(EmployeeDto employeeDto) {
		EmployeeResponse employeeResponse = employeeValidatorService.validate(employeeDto);
		if (employeeResponse.getResponseStatus() == ResponseStatus.SUCCESS) {
			generateUuid(employeeDto);
			producerService.produce(employeeDto);
		}
		evictEmployeeCache(employeeDto.getUuid());
		return employeeResponse;
	}

	@Override
	public EmployeeDto findByUuid(String uuid) {
		String cached = stringRedisTemplate.opsForValue().get(EMPLOYEE_CACHE_PREFIX + uuid);
		if (cached != null) {
			try {
				return objectMapper.readValue(cached, EmployeeDto.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cached employee {}", uuid, e);
			}
		}

		Employee employee = employeeRepository
				.findEmployeeByUuid(uuid)
				.orElseThrow(() -> new EmployeeNotFoundException(UUID_NOT_FOUND));
		EmployeeDto dto = toDto(employee);

		try {
			stringRedisTemplate.opsForValue().set(
					EMPLOYEE_CACHE_PREFIX + uuid, objectMapper.writeValueAsString(dto),
					CACHE_TTL_MINUTES, TimeUnit.MINUTES);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize employee {} for cache", uuid, e);
		}
		return dto;
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
		evictEmployeeCache(uuid);
		return save(employeeDtoFromRepo);
	}

	@Override
	public EmployeeResponse assignTaskToEmployee(String uuid, long taskId) {
		EmployeeDto employeeDto = findByUuid(uuid);
		TaskDto taskDto = taskService.findById(taskId);
		employeeDto.getTasks().add(toEntity(taskDto));
		evictEmployeeCache(uuid);
		return save(employeeDto);
	}

	@Override
	public EmployeeResponse unAssignTaskFromEmployee(String uuid, long taskId) {
		EmployeeDto employeeDto = findByUuid(uuid);
		TaskDto taskDto = taskService.findById(taskId);
		employeeDto.getTasks().remove(toEntity(taskDto));
		evictEmployeeCache(uuid);
		return save(employeeDto);
	}

	private void evictEmployeeCache(String uuid) {
		stringRedisTemplate.delete(EMPLOYEE_CACHE_PREFIX + uuid);
		stringRedisTemplate.delete(EMPLOYEE_ALL_CACHE);
		stringRedisTemplate.delete(PDF_CACHE_PREFIX + uuid);
	}

	private byte[] getCachedPdf(String uuid) {
		String cached = stringRedisTemplate.opsForValue().get(PDF_CACHE_PREFIX + uuid);
		if (cached != null) {
			try {
				return java.util.Base64.getDecoder().decode(cached);
			} catch (Exception e) {
				log.warn("Failed to decode cached PDF for {}", uuid, e);
			}
		}
		return null;
	}

	private void cachePdf(String uuid, byte[] pdfBytes) {
		try {
			stringRedisTemplate.opsForValue().set(
					PDF_CACHE_PREFIX + uuid,
					java.util.Base64.getEncoder().encodeToString(pdfBytes),
					10, TimeUnit.MINUTES);
		} catch (Exception e) {
			log.warn("Failed to cache PDF for {}", uuid, e);
		}
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
