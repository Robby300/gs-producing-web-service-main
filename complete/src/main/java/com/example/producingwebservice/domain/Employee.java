package com.example.producingwebservice.domain;


import https.www_rob_com.gen.Position;
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
    private Position position;

    public Employee(Long id, String name, int salary, Position position) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.position = position;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return salary == employee.salary && Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && position == employee.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, position);
    }
}
