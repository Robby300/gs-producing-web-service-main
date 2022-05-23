package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
import com.example.producingwebservice.mapper.EmployeeMapper;
import com.example.producingwebservice.repository.EmployeeRepository;
import https.www_rob_com.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    public GetEmployeeDetailsResponse getEmployeeDetails(GetEmployeeDetailsRequest request) {
        GetEmployeeDetailsResponse response = new GetEmployeeDetailsResponse();
        response.setEmployeeDetails(employeeMapper.mapToEmployeeDetails(employeeRepository.findById(request.getId()).orElseThrow(RuntimeException::new)));
        return response;
    }

    public GetAllEmployeeDetailsResponse getGetAllEmployeeDetails() {
        GetAllEmployeeDetailsResponse allEmployeeDetailsResponse = new GetAllEmployeeDetailsResponse();
        Iterable<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            GetEmployeeDetailsResponse employeeDetailsResponse = mapEmployeeToGetResponse(employee);
            allEmployeeDetailsResponse.getEmployeeDetails().add(employeeDetailsResponse.getEmployeeDetails());
        }

        return allEmployeeDetailsResponse;
    }

    public CreateEmployeeDetailsResponse saveEmployeeDetails(CreateEmployeeDetailsRequest request) {
        EmployeeDetails employeeDetails = request.getEmployeeDetails();
        Employee employeeToSave = employeeMapper.mapToEmployee(employeeDetails);

        CreateEmployeeDetailsResponse createEmployeeDetailsResponse = new CreateEmployeeDetailsResponse();
        EmployeePosition employeeToSavePosition = employeeToSave.getEmployeePosition();
        if (employeeToSavePosition.isValidSalary(employeeToSave.getSalary())) {
            employeeRepository.save(employeeToSave);
            createEmployeeDetailsResponse.setEmployeeDetails(employeeDetails);
            createEmployeeDetailsResponse.setMessage("New employee was created successfully");
        } else {
            createEmployeeDetailsResponse.setMessage(employeeToSavePosition.getNotValidMessage(employeeToSave.getSalary()));
        }
        return createEmployeeDetailsResponse;
    }

    public UpdateEmployeeDetailsResponse updateEmployeeDetails(UpdateEmployeeDetailsRequest request) {
        UpdateEmployeeDetailsResponse employeeDetailsResponse = null;
        Optional<Employee> existingEmployee = this.employeeRepository.findById(request.getEmployeeDetails().getId());
        if (existingEmployee.isEmpty()) {
            employeeDetailsResponse = mapEmployeeToUpdateResponse(null, "Id not found");
        }
        if (existingEmployee.isPresent()) {

            Employee employeeToUpdate = existingEmployee.orElseThrow(RuntimeException::new);
            employeeToUpdate.setName(request.getEmployeeDetails().getName());
            employeeToUpdate.setSalary(request.getEmployeeDetails().getSalary());
            employeeToUpdate.setEmployeePosition(EmployeePosition.valueOf(request.getEmployeeDetails().getEmployeeDetailsPosition().value()));

            EmployeePosition employeeToUpdatePosition = employeeToUpdate.getEmployeePosition();
            if (employeeToUpdatePosition.isValidSalary(employeeToUpdate.getSalary())) {
                employeeRepository.save(employeeToUpdate);
                employeeDetailsResponse = mapEmployeeToUpdateResponse(employeeToUpdate, "Updated successfully");
            } else {
                employeeDetailsResponse.setMessage(employeeToUpdatePosition.getNotValidMessage(employeeToUpdate.getSalary()));
            }
        }
        return employeeDetailsResponse;
    }

    public DeleteEmployeeDetailsResponse deleteEmployeeDetails(DeleteEmployeeDetailsRequest request) {
        employeeRepository.deleteById(request.getId());

        DeleteEmployeeDetailsResponse courseDetailsResponse = new DeleteEmployeeDetailsResponse();
        courseDetailsResponse.setMessage("Deleted Successfully");
        return courseDetailsResponse;
    }


    private GetEmployeeDetailsResponse mapEmployeeToGetResponse(Employee employee) {
        EmployeeDetails employeeDetails = employeeMapper.mapToEmployeeDetails(employee);
        GetEmployeeDetailsResponse employeeDetailsResponse = new GetEmployeeDetailsResponse();

        employeeDetailsResponse.setEmployeeDetails(employeeDetails);
        return employeeDetailsResponse;
    }

    private UpdateEmployeeDetailsResponse mapEmployeeToUpdateResponse(Employee employee, String message) {
        EmployeeDetails employeeDetails = employeeMapper.mapToEmployeeDetails(employee);
        UpdateEmployeeDetailsResponse employeeDetailsResponse = new UpdateEmployeeDetailsResponse();

        employeeDetailsResponse.setEmployeeDetails(employeeDetails);
        employeeDetailsResponse.setMessage(message);
        return employeeDetailsResponse;
    }
}
