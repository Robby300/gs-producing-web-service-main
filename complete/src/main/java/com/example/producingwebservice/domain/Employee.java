package com.example.producingwebservice.domain;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 32, message = "Name should be between 2 and 32 characters")
    @UniqueElements(message = "Name should be unique")
    private String name;

    @Min(value = 0, message = "salary should be greater than 0")
    private int salary;

    @Enumerated(EnumType.STRING)
    private EmployeePosition employeePosition;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employee_tasks",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_uuid")
    )
    private List<Task> tasks;
}
