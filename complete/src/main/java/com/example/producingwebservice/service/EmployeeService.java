package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.exception.NotValidException;
import com.example.producingwebservice.mapper.EmployeeMapper;
import com.example.producingwebservice.repository.EmployeeRepository;
import https.www_rob_com.gen.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final static String ID_NOT_FOUND_MESSAGE = "Id not found";
    private final static String EMPLOYEE_CREATED = "New employee was created successfully";
    private final static String EMPLOYEE_DELETED = "Employee was deleted Successfully";
    private final static String EMPLOYEE_UPDATED = "Employee was updated successfully";
    private final EmployeeMapper employeeMapper = new EmployeeMapper();
    private final EmployeeRepository employeeRepository;
    private final MessageService messageService;

    public GetEmployeeDetailsResponse getEmployeeDetails(GetEmployeeDetailsRequest request) {
        log.info("Get employeeDetails by id = {}", request.getId());
        Employee employeeFromRepo =
                employeeRepository.findById(request.getId())
                        .orElseThrow(() -> new EmployeeNotFoundException(ID_NOT_FOUND_MESSAGE));
        GetEmployeeDetailsResponse response = new GetEmployeeDetailsResponse();
        response.setEmployeeDetails(employeeMapper.toView(employeeFromRepo));
        return response;
    }

    public GetAllEmployeeDetailsResponse getAllEmployeeDetails() {
        log.info("Get all employeeDetails");
        GetAllEmployeeDetailsResponse allEmployeeDetailsResponse = new GetAllEmployeeDetailsResponse();
        Iterable<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            GetEmployeeDetailsResponse employeeDetailsResponse = mapEmployeeToGetResponse(employee);
            allEmployeeDetailsResponse.getEmployeeDetails().add(employeeDetailsResponse.getEmployeeDetails());
        }
        return allEmployeeDetailsResponse;
    }

    public CreateEmployeeDetailsResponse saveEmployeeDetails(CreateEmployeeDetailsRequest request) {
        log.info("Create and save employeeDetails with name = {}", request.getEmployeeDetails().getName());
        EmployeeDetails employeeDetails = request.getEmployeeDetails();
        Employee employeeToSave = employeeMapper.fromView(employeeDetails);

        CreateEmployeeDetailsResponse createEmployeeDetailsResponse = new CreateEmployeeDetailsResponse();
        EmployeePosition employeeToSavePosition = employeeToSave.getEmployeePosition();
        if (employeeToSavePosition.isValidSalary(employeeToSave.getSalary())) {
            employeeRepository.save(employeeToSave);
            createEmployeeDetailsResponse.setEmployeeDetails(employeeDetails);
            createEmployeeDetailsResponse.setMessage(EMPLOYEE_CREATED); //todo вынести в константу // done
        } else {
            String salaryNotValidMessage =
                    messageService.getNotValidSalaryMessage(employeeToSavePosition, employeeToSave.getSalary());
            createEmployeeDetailsResponse.setMessage(salaryNotValidMessage);
            throw new NotValidException(salaryNotValidMessage);
        }
        return createEmployeeDetailsResponse;
    }

    public UpdateEmployeeDetailsResponse updateEmployeeDetails(UpdateEmployeeDetailsRequest request) {
        log.info("Update employeeDetails by id = {}", request.getEmployeeDetails().getId());

        Optional<Employee> existingEmployee = employeeRepository.findById(request.getEmployeeDetails().getId());
        UpdateEmployeeDetailsResponse employeeDetailsResponse = new UpdateEmployeeDetailsResponse();
        if (existingEmployee.isEmpty()) {
            employeeDetailsResponse.setMessage(ID_NOT_FOUND_MESSAGE);
            return employeeDetailsResponse;
        } else {
            Employee employeeToUpdate = employeeMapper.fromView(request.getEmployeeDetails());
            EmployeePosition employeeToUpdatePosition = employeeToUpdate.getEmployeePosition();
            return getUpdateEmployeeDetailsResponse(employeeToUpdate, employeeToUpdatePosition);
        }
    }

    public DeleteEmployeeDetailsResponse deleteEmployeeDetails(DeleteEmployeeDetailsRequest request) {
        log.info("Delete employeeDetails by id = {}", request.getId());
        employeeRepository.deleteById(request.getId());

        DeleteEmployeeDetailsResponse courseDetailsResponse = new DeleteEmployeeDetailsResponse();
        courseDetailsResponse.setMessage(EMPLOYEE_DELETED); //todo вынести в константу //done
        return courseDetailsResponse;
    }

    private GetEmployeeDetailsResponse mapEmployeeToGetResponse(Employee employee) {
        EmployeeDetails employeeDetails = employeeMapper.toView(employee);
        GetEmployeeDetailsResponse employeeDetailsResponse = new GetEmployeeDetailsResponse();
        employeeDetailsResponse.setEmployeeDetails(employeeDetails);
        return employeeDetailsResponse;
    }

    private UpdateEmployeeDetailsResponse getUpdateEmployeeDetailsResponse(Employee employeeToUpdate, EmployeePosition employeeToUpdatePosition) {
        UpdateEmployeeDetailsResponse employeeDetailsResponse = new UpdateEmployeeDetailsResponse();
        if (employeeToUpdatePosition.isValidSalary(employeeToUpdate.getSalary())) {
            employeeRepository.save(employeeToUpdate);
            employeeDetailsResponse.setEmployeeDetails(employeeMapper.toView(employeeToUpdate));
            employeeDetailsResponse.setMessage(EMPLOYEE_UPDATED); //todo вынести в константу // done
        } else {
            employeeDetailsResponse.setMessage(messageService.getNotValidSalaryMessage(employeeToUpdatePosition, employeeToUpdate.getSalary()));
        }
        return employeeDetailsResponse;
    }
}
