package com.example.producingwebservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"id"})
public class TaskDto {
    private long id;
    private String description;
}
