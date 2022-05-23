package com.example.producingwebservice.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
