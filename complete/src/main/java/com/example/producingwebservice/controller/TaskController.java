package com.example.producingwebservice.controller;

import com.example.producingwebservice.domain.Task;
import com.example.producingwebservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public List<Task> findAll() {
        return taskService.findAll();
    }

    @PostMapping()
    public void create(@RequestBody List<Task> tasks) {
        log.info("POST request received with parameter = {}", tasks);
        tasks.forEach(taskService::save);
    }


    @GetMapping("/{id}")
    public Task getById(@PathVariable("id") Task task) {
        return task;
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable("id") Task taskFromRepo,
                           @RequestBody Task task) {
        BeanUtils.copyProperties(task, taskFromRepo, "id");
        return taskService.save(taskFromRepo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Task employee) {
        taskService.delete(employee);
    }
}
