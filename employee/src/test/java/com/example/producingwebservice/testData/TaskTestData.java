package com.example.producingwebservice.testData;

import com.example.producingwebservice.model.TaskDto;

import java.util.List;

public class TaskTestData {

    public static TaskDto getFirstTask() {
        return new TaskDto(null, "first test task");
    }
    public static TaskDto getSecondTask() {
        return new TaskDto(null, "second test task");
    }
    public static TaskDto getThirdTask() {
        return new TaskDto(null, "third test task");
    }

    public static TaskDto getFourthTask() {
        return new TaskDto(null, "fourth test task");
    }

    public static List<TaskDto> getTaskDtoList() {
        return List.of(new TaskDto(null, "fifth test task"),
                new TaskDto(null, "sixth test task"));
    }
}
