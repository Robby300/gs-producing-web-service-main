package com.example.producingwebservice.controller;


import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.model.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1.0/tasks")
@Tag(name = "Task controller")
public class TaskController {

	private final TaskService taskService;

	@GetMapping()
	@Operation(summary = "Find all tasks")
	public List<TaskDto> findAll() {
		log.info("Find all tasks");
		return taskService.findAll();
	}

	@PostMapping()
	@Operation(summary = "Create all tasks")
	public List<TaskDto> create(@RequestBody List<TaskDto> taskDtos) {
		log.info("POST request received with parameter = {} to create new {} tasks", taskDtos, taskDtos.size());
		return taskService.saveAll(taskDtos);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Find task by id")
	public TaskDto getById(@PathVariable("id") Long id) {
		log.info("Get task by id = {}", id);
		return taskService.findById(id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update task by id")
	public TaskDto update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
		log.info("Update task by id = {}", id);
		return taskService.update(id, taskDto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete task by id")
	public void delete(@PathVariable("id") Long id) {
		log.info("Delete task by id = {}", id);
		taskService.deleteById(id);
	}
}
