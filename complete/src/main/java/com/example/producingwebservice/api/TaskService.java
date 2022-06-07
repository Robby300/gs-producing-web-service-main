package com.example.producingwebservice.api;

import com.example.producingwebservice.domain.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
    Task save(Task task);
    void delete(Task task);
}
