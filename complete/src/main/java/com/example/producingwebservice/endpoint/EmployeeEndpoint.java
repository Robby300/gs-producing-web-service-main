package com.example.producingwebservice.endpoint;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import https.www_rob_com.gen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class EmployeeEndpoint {
    private static final String NAMESPACE_URI = "https://www.rob.com/gen";

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeEndpoint(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createEmployeeDetailsRequest")
    @ResponsePayload
    public CreateEmployeeDetailsRequest save(@RequestPayload CreateEmployeeDetailsRequest request) {
        EmployeeDetails employeeDetails = request.getEmployeeDetails();
        Employee employee = new Employee();
        employee.setName(employeeDetails.getName());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());
        employeeRepository.save(employee);
        return request;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEmployeeDetailsRequest")
    @ResponsePayload
    public GetEmployeeDetailsResponse getEmployee(@RequestPayload GetEmployeeDetailsRequest request) {
        GetEmployeeDetailsResponse response = new GetEmployeeDetailsResponse();
        response.setEmployeeDetails(employeeRepository.findById(request.getId()).orElseThrow(RuntimeException::new).mapToEmployeeDetails());
        return response;
    }
}
