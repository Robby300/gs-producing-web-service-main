package com.example.producingwebservice.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int salary;
    @Enumerated(EnumType.STRING)
    private EmployeePosition employeePosition;

    public Employee(Long id, String name, int salary, EmployeePosition employeePosition) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.employeePosition = employeePosition;
    }

    public Employee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public EmployeePosition getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(EmployeePosition employeePosition) {
        this.employeePosition = employeePosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return salary == employee.salary && Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && employeePosition == employee.employeePosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, employeePosition);
    }
}
