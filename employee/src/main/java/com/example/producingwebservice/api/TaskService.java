package com.example.producingwebservice.api;


import com.example.producingwebservice.model.TaskDto;

import java.util.List;

public interface TaskService {
	List<TaskDto> findAll();

	TaskDto findById(Long id);

	TaskDto update(Long id, TaskDto taskDto);

	void deleteById(Long id);

	TaskDto save(TaskDto taskDto);

	List<TaskDto> saveAll(List<TaskDto> taskDtos);
}
