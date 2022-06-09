package com.example.producingwebservice.controller;

import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.model.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public List<TaskDto> findAll() {
        log.info("Find all tasks");
        return taskService.findAll();
    }

    @PostMapping()
    public List<TaskDto> create(@RequestBody List<TaskDto> taskDtos) {
        log.info("POST request received with parameter = {} to create new {} tasks", taskDtos, taskDtos.size());
        return taskService.saveAll(taskDtos);
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable("id") Long id) {
        log.info("Get task by id = {}", id);
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable("id") Long id,
                          @RequestBody TaskDto taskDto) {
        log.info("Update task by id = {}", id);
        return taskService.update(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Delete task by id = {}", id);
        taskService.deleteById(id);
    }
}
