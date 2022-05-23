package com.example.producingwebservice.mapper;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
import https.www_rob_com.gen.EmployeeDetails;
import https.www_rob_com.gen.EmployeeDetailsPosition;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee mapToEmployee(EmployeeDetails employeeDetails) {
        return Employee.builder()
                .id(employeeDetails.getId())
                .name(employeeDetails.getName())
                .employeePosition(mapToEmployeePosition(employeeDetails.getEmployeeDetailsPosition()))
                .salary(employeeDetails.getSalary())
                .build();
    }

    public EmployeeDetails mapToEmployeeDetails(Employee employee) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setId(employee.getId());
        employeeDetails.setName(employee.getName());
        employeeDetails.setSalary(employee.getSalary());
        employeeDetails.setEmployeeDetailsPosition(mapToEmployeeDetailsPosition(employee.getEmployeePosition()));
        return employeeDetails;
    }

    private EmployeePosition mapToEmployeePosition(EmployeeDetailsPosition position) {
        return EmployeePosition.valueOf(position.value());
    }

    private EmployeeDetailsPosition mapToEmployeeDetailsPosition(EmployeePosition position) {
        return EmployeeDetailsPosition.valueOf(position.value());
    }
}