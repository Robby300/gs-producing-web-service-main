package com.example.producingwebservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(exclude = {"id"})
public class TaskDto {
    private long id;
    private String description;
}
