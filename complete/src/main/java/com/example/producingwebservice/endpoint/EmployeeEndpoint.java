package com.example.producingwebservice.endpoint;

import com.example.producingwebservice.service.EmployeeService;
import https.www_rob_com.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.validation.Valid;

@Endpoint
@RequiredArgsConstructor
public class EmployeeEndpoint {
    private static final String NAMESPACE_URI = "https://www.rob.com/gen";

    private final EmployeeService employeeService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetEmployeeDetailsRequest")
    @ResponsePayload
    public GetEmployeeDetailsResponse getEmployee(@RequestPayload @Valid GetEmployeeDetailsRequest request) {
        return employeeService.getEmployeeDetails(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllEmployeeDetailsRequest")
    @ResponsePayload
    public GetAllEmployeeDetailsResponse findAll() {
        return employeeService.getAllEmployeeDetails();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateEmployeeDetailsRequest")
    @ResponsePayload
    public CreateEmployeeDetailsResponse save(@RequestPayload CreateEmployeeDetailsRequest request) {
        return employeeService.saveEmployeeDetails(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateEmployeeDetailsRequest")
    @ResponsePayload
    public UpdateEmployeeDetailsResponse update(@RequestPayload UpdateEmployeeDetailsRequest request) {
        return employeeService.updateEmployeeDetails(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteEmployeeDetailsRequest")
    @ResponsePayload
    public DeleteEmployeeDetailsResponse delete(@RequestPayload DeleteEmployeeDetailsRequest request) {
        return employeeService.deleteEmployeeDetails(request);
    }
}
