package com.example.producingwebservice.domain;

import com.example.producingwebservice.type.EmployeePosition;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    @Size(min = 3, max = 32, message = "Name should be between 2 and 32 characters")
    private String name;

    @Min(value = 1, message = "salary should be greater than 0")
    private int salary;

    @NotNull(message = "Please provide a position")
    @Enumerated(EnumType.STRING)
    private EmployeePosition employeePosition;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "employee_tasks",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> tasks;
}
