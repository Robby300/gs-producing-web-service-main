package com.example.producingwebservice.model;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.type.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class EmployeeDto {
    private long id;
    private String uuid;
    private String name;
    private String salary;
    private Position position;
    private Set<Task> tasks;
}
