package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task save(Task task);

    void delete(Task task);
}
