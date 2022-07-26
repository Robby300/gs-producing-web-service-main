package com.example.producingwebservice.model;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.type.Position;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tasks"})
@ToString(exclude = {"id", "uuid"})
public class EmployeeDto {
    private long id;
    private String uuid;
    private String name;
    private String salary;
    private Position position;
    private Set<Task> tasks;
}
