package com.example.producingwebservice.testData;

import com.example.producingwebservice.model.TaskDto;

public class TaskTestData {

    public static TaskDto getFirstTask() {
        return new TaskDto(1L, "first test task");
    }
    public static TaskDto getSecondTask() {
        return new TaskDto(2L, "second test task");
    }
    public static TaskDto getThirdTask() {
        return new TaskDto(3L, "third test task");
    }

    public static TaskDto getFourthTask() {
        return new TaskDto(4L, "fourth test task");
    }
}
