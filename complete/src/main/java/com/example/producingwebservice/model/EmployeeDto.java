package com.example.producingwebservice.model;

import com.example.producingwebservice.domain.Task;
import com.example.producingwebservice.type.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class EmployeeDto {
    private String name;
    private String salary;
    private Position position;
    private Set<Task> tasks;
}
