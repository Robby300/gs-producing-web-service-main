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
    public GetAllEmployeeDetailsResponse findAll(@RequestPayload GetAllEmployeeDetailsRequest request) {

        GetAllEmployeeDetailsResponse allEmployeeDetailsResponse = new GetAllEmployeeDetailsResponse();
        Iterable<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            GetEmployeeDetailsResponse courseDetailsResponse = mapEmployeeDetails(employee);
            allEmployeeDetailsResponse.getEmployeeDetails().add(courseDetailsResponse.getEmployeeDetails());
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
        if (existingEmployee.isEmpty() || existingEmployee == null) {
            employeeDetailsResponse = mapEmployeeDetail(null, "Id not found");
        }
        if (existingEmployee.isPresent()) {

            Employee employee = existingEmployee.orElseThrow(RuntimeException::new);
            employee.setName(request.getEmployeeDetails().getName());
            employee.setSalary(request.getEmployeeDetails().getSalary());
            employee.setEmployeePosition(EmployeePosition.valueOf(request.getEmployeeDetails().getEmployeeDetailsPosition().value()));

            employeeRepository.save(employee);
            employeeDetailsResponse = mapEmployeeDetail(employee, "Updated successfully");

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

    private GetEmployeeDetailsResponse mapEmployeeDetails(Employee employee) {
        EmployeeDetails employeeDetails = employeeMapper.mapToEmployeeDetails(employee);

        GetEmployeeDetailsResponse employeeDetailsResponse = new GetEmployeeDetailsResponse();

        employeeDetailsResponse.setEmployeeDetails(employeeDetails);
        ;
        return employeeDetailsResponse;
    }

    private UpdateEmployeeDetailsResponse mapEmployeeDetail(Employee employee, String message) {
        EmployeeDetails employeeDetails = employeeMapper.mapToEmployeeDetails(employee);
        UpdateEmployeeDetailsResponse courseDetailsResponse = new UpdateEmployeeDetailsResponse();

        courseDetailsResponse.setEmployeeDetails(employeeDetails);
        courseDetailsResponse.setMessage(message);
        return courseDetailsResponse;
    }
}
