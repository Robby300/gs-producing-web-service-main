package com.example.producingwebservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskDto {
    private long id;
    private String description;
}
