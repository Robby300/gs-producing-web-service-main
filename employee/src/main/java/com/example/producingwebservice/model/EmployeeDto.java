package com.example.producingwebservice.model;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.type.Position;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "tasks"})
@ToString(exclude = {"id", "uuid"})
public class EmployeeDto {
    private Long id;
    private String uuid;
    private String name;
    private String salary;
    private Position position;
    private Set<Task> tasks;
}
