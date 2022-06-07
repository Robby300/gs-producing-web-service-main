package com.example.producingwebservice.domain;

import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.type.Position;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@ToString(exclude = {"id", "uuid"})
public class Employee {
    public Employee(EmployeeDto employeeDto) {
        setName(employeeDto.getName());
        setSalary(employeeDto.getSalary());
        setPosition(employeeDto.getPosition());
        setTasks(employeeDto.getTasks());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
    private String name;
    private String salary;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "employee_tasks",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> tasks;
}
