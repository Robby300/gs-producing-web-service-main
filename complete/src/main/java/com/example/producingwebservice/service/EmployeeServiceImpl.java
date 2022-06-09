package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.api.EmployeeValidatorService;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private static final String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final EmployeeRepository employeeRepository;
    private final ProducerService producerService;
    private final EmployeeValidatorService employeeValidatorService;
    private final ModelMapper modelMapper;

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
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
        Employee employee =
                employeeRepository
                        .findEmployeeByUuid(uuid)
                        .orElseThrow(() -> new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeResponse> saveAll(List<EmployeeDto> employeeDtos) {
        return employeeDtos
                .stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse update(String uuid, EmployeeDto employeeDto) {
        EmployeeDto employeeDtoFromRepo = findByUuid(uuid);
        BeanUtils.copyProperties(employeeDto, employeeDtoFromRepo, "id", "uuid");
        log.info("Update employee = {}", employeeDtoFromRepo);
        return save(employeeDtoFromRepo);
    }

    @Override
    public EmployeeResponse assignTaskToEmployee(String uuid, TaskDto taskDto) {
        EmployeeDto employeeDto = findByUuid(uuid);
        employeeDto.getTasks().add(modelMapper.map(taskDto, Task.class));
        return save(employeeDto);
    }

    @Override
    public EmployeeResponse unAssignTaskFromEmployee(String uuid, TaskDto taskDto) {
        EmployeeDto employeeDto = findByUuid(uuid);
        employeeDto.getTasks().remove(modelMapper.map(taskDto, Task.class));
        return save(employeeDto);
    }

    private void generateUuid(EmployeeDto employeeDto) {
        if (employeeDto.getUuid() == null) {
            employeeDto.setUuid(UUID.randomUUID().toString());
        }
    }
}
