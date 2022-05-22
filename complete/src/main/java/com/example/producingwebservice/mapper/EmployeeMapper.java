package com.example.producingwebservice.mapper;

import com.example.producingwebservice.domain.Employee;
import https.www_rob_com.gen.EmployeeDetails;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee mapToEmployee(EmployeeDetails employeeDetails) {
        return Employee.builder()
                .id(employeeDetails.getId())
                .name(employeeDetails.getName())
                .position(employeeDetails.getPosition())
                .salary(employeeDetails.getSalary())
                .build();
    }

    public EmployeeDetails mapToEmployeeDetails(Employee employee) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setId(employee.getId());
        employeeDetails.setName(employee.getName());
        employeeDetails.setSalary(employee.getSalary());
        employeeDetails.setPosition(employee.getPosition());
        return employeeDetails;
    }
}
