package com.example.producingwebservice.endpoint;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
import com.example.producingwebservice.mapper.EmployeeMapper;
import com.example.producingwebservice.repository.EmployeeRepository;
import https.www_rob_com.gen.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

@Endpoint
public class EmployeeEndpoint {
    private static final String NAMESPACE_URI = "https://www.rob.com/gen";

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeEndpoint(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetEmployeeDetailsRequest")
    @ResponsePayload
    public GetEmployeeDetailsResponse getEmployee(@RequestPayload GetEmployeeDetailsRequest request) {
        GetEmployeeDetailsResponse response = new GetEmployeeDetailsResponse();
        response.setEmployeeDetails(employeeMapper.mapToEmployeeDetails(employeeRepository.findById(request.getId()).orElseThrow(RuntimeException::new)));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllEmployeeDetailsRequest")
    @ResponsePayload
    public GetAllEmployeeDetailsResponse findAll() {

        GetAllEmployeeDetailsResponse allEmployeeDetailsResponse = new GetAllEmployeeDetailsResponse();
        Iterable<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            GetEmployeeDetailsResponse employeeDetailsResponse = mapEmployeeToGetResponse(employee);
            allEmployeeDetailsResponse.getEmployeeDetails().add(employeeDetailsResponse.getEmployeeDetails());
        }

        return allEmployeeDetailsResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateEmployeeDetailsRequest")
    @ResponsePayload
    public CreateEmployeeDetailsResponse save(@RequestPayload CreateEmployeeDetailsRequest request) {

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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateEmployeeDetailsRequest")
    @ResponsePayload
    public UpdateEmployeeDetailsResponse update(@RequestPayload UpdateEmployeeDetailsRequest request) {
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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteEmployeeDetailsRequest")
    @ResponsePayload
    public DeleteEmployeeDetailsResponse delete(@RequestPayload DeleteEmployeeDetailsRequest request) {

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
