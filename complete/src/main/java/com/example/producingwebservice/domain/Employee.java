package com.example.producingwebservice.domain;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    @NotEmpty(message = "Name should not be empty") //на перспективу для REST
    @Size(min = 2, max = 32, message = "Name should be between 2 and 32 characters")
    @UniqueElements(message = "Name should be unique")
    private String name;

    @Min(value = 0, message = "salary should be greater than 0")
    private int salary;

    @Enumerated(EnumType.STRING)
    private EmployeePosition employeePosition;
}
